import datetime
from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from django.contrib.auth.tokens import default_token_generator
from django.core.validators import validate_email
from django.http import JsonResponse
from django.shortcuts import render, redirect
from django.utils.encoding import force_text
from django.utils.http import urlsafe_base64_decode
from rest_framework import viewsets, status
from rest_framework.decorators import api_view, authentication_classes, permission_classes
from rest_framework.exceptions import PermissionDenied
from rest_framework.permissions import IsAuthenticatedOrReadOnly, AllowAny
from rest_framework.response import Response

from django.contrib.auth.decorators import login_required
from django.shortcuts import render, get_object_or_404, redirect
from django.template import loader
from django.http import HttpResponse
from django import template

from server.forms import LoginForm, AdviceForm
from server.permissions import IsOwner
from server.serializers import *
from server.r_tasks import tasks
from django.forms.models import model_to_dict
from fcm_django.models import FCMDevice



from django.db.models.aggregates import Count
from random import randint

# Create your views here.




class CarsViewSet(viewsets.ModelViewSet):
    queryset = Car.objects.all()
    serializer_class = CarSerializer
    permission_classes = [IsOwner,IsAuthenticatedOrReadOnly,]


    def get_queryset(self):
        user = self.request.user
        if user.is_authenticated:
            return Car.objects.filter(owner=user)
        raise PermissionDenied()

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)

    def perform_update(self, serializer):
        serializer.save(owner=self.request.user)


class ServiceCenterViewSet(viewsets.ModelViewSet):
    queryset = ServiceCenter.objects.all()
    serializer_class = ServiceCenterSerializer
    permission_classes = [IsOwner,IsAuthenticatedOrReadOnly,]


    def get_queryset(self):
        user = self.request.user
        if user.is_authenticated:
            queryset = ServiceCenter.objects.filter(owner=user)
            return queryset
        raise PermissionDenied()

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)

    def perform_update(self, serializer):
        serializer.save(owner=self.request.user)



class EngineOilViewSet(viewsets.ModelViewSet):
    queryset = EngineOil.objects.all()
    serializer_class = EngineOilSerializer
    permission_classes = [IsOwner,IsAuthenticatedOrReadOnly,]

    def perform_create(self, serializer):
        serializer.save()

    def perform_update(self, serializer):
        serializer.save()






class ChangeViewSet(viewsets.ModelViewSet):
    queryset = Change.objects.all()
    serializer_class = ChangeSerializer
    permission_classes = [IsOwner,IsAuthenticatedOrReadOnly,]

    def perform_create(self, serializer):
        serializer.save()

    def perform_update(self, serializer):
        serializer.save()





class StatusViewSet(viewsets.ModelViewSet):
    queryset = Status.objects.all()
    serializer_class = StatusSerializer
    permission_classes = [IsOwner,IsAuthenticatedOrReadOnly,]

    def perform_create(self, serializer):
        serializer.save()

    def perform_update(self, serializer):
        serializer.save()


@api_view(['GET'])
def getVerificationByCar(request,pk):
    if request.method =='GET':
        queryset = Verification.objects.all()
        car = Car.objects.get(id=pk)
        queryset = Verification.objects.filter(car=car)
        print(queryset)

        verif_serialis = VerificationSerializer(queryset,many=True)
        return JsonResponse(verif_serialis.data,safe=False)

@api_view(['GET'])
def getReparationByCar(request,pk):
    if request.method == 'GET':
        queryset = Reparation.objects.all()
        print(queryset)
        car = Car.objects.get(id=pk)
        queryset = Reparation.objects.filter(car=car)
        print(queryset)

        repare_serialis = ReparationSerializer(queryset,many=True)
        return JsonResponse(repare_serialis.data,safe=False)



class VerificationViewSet(viewsets.ModelViewSet):
    queryset = Verification.objects.all()
    serializer_class = VerificationSerializer
    permission_classes = [IsOwner,IsAuthenticatedOrReadOnly,]


    def get_queryset(self):
        queryset = Verification.objects.all()
        car2 = self.request.data
        print(car2)
        car = Car.objects.get(id=car2['id'])
        queryset = Verification.objects.filter(car = car)

        return queryset

    def create(self, request):
        car = request.data.pop('car')
        carvalue = Car.objects.get(id=car['id'])
        service_center = request.data.pop('service_center')
        service_centervalue = ServiceCenter.objects.get(id=service_center['id'])
        engine_oil = request.data.pop('engine_oil')
        engine_oilvalue = EngineOil.objects.get(id=engine_oil['id'])
        stat = request.data.pop('states')
        statvalue = Status.objects.get(id=stat['id'])
        change = request.data.pop('changes')
        changevalue = Change.objects.get(id=change['id'])
        date = request.data.pop('date')
        odometer = request.data.pop('odometer')
        note = request.data.pop('note')
        id_vir = request.data.pop('id')
        verification = Verification.objects.create(id =id_vir,date=date,note=note,odometer=odometer,car = carvalue,service_center = service_centervalue,engine_oil = engine_oilvalue,states=statvalue,changes=changevalue)
        if verification.id is not None:
            """carvalue = model_to_dict(carvalue)"""
            changevalue = model_to_dict(changevalue)
            engine_oilvalue = model_to_dict(engine_oilvalue)

            tasks.start_predicting_maintenance(car,engine_oilvalue,odometer,changevalue)
            return Response(status=status.HTTP_201_CREATED,data=request.data)
        else:
            return Response(data=None)



class ReparationViewSet(viewsets.ModelViewSet):
    queryset = Reparation.objects.all()
    serializer_class = ReparationSerializer
    permission_classes = [IsOwner, IsAuthenticatedOrReadOnly, ]


    def get_queryset(self):
        queryset = Verification.objects.all()
        car2 = self.request.GET.get(0)
        print(car2)
        car = Car.objects.get(id=car2['id'])
        queryset = Verification.objects.filter(car=car)

        return queryset

    def create(self, request):
        car = request.data.pop('car')
        carvalue = Car.objects.get(id=car['id'])
        service_center = request.data.pop('service_center')
        service_centervalue = ServiceCenter.objects.get(id=service_center['id'])
        date = request.data.pop('date')
        odometer = request.data.pop('odometer')
        note = request.data.pop('note')
        id_repare = request.data.pop('id')
        what_repared = request.data.pop('whatRepared')
        reparation = Reparation.objects.create(id=id_repare, date=date, note=note, odometer=odometer, car=carvalue,
                                                   service_center=service_centervalue,what_repared=what_repared)
        if reparation.id is not None:
            return Response(status=status.HTTP_201_CREATED, data=request.data)
        else:
            return Response(data=None)


@api_view(['GET'])
def getPredictedMaintenanceByCar(request,pk):
    if request.method == 'GET':
        queryset = PredictedMaintenance.objects.all()
        print(queryset)
        car = Car.objects.get(id=pk)
        queryset = PredictedMaintenance.objects.filter(car=car)
        print(queryset)

        repare_serialis = PredictiveMaintenanceSerializer(queryset,many=True)
        return JsonResponse(repare_serialis.data,safe=False)

@api_view(['GET'])
def getAllPredictedMaintenance(request):
    if request.method == 'GET':
        queryset = PredictedMaintenance.objects.all()
        print(queryset)

        repare_serialis = PredictiveMaintenanceSerializer(queryset,many=True)
        return JsonResponse(repare_serialis.data,safe=False)


@api_view(['GET'])
def getLastPredictedMaintenance(request):
    if request.method == 'GET':
        queryset = PredictedMaintenance.objects.all().order_by('-date').first()
        print(queryset)

        repare_serialis = PredictiveMaintenanceSerializer(queryset)
        return JsonResponse(repare_serialis.data,safe=False)



class PredictedMaintenanceViewSet(viewsets.ModelViewSet):
    queryset = PredictedMaintenance.objects.all()
    serializer_class = PredictiveMaintenanceSerializer
    permission_classes = [IsOwner, IsAuthenticatedOrReadOnly, ]


    def get_queryset(self):
        user = self.request.user
        if user.is_authenticated:
            car = Car.objects.filter(owner=user)
            print(car)
            queryset = PredictedMaintenance.objects.filter(car__in=car).order_by('-date')
            return queryset

        raise PermissionDenied()



@api_view(['GET'])
def activateUserAccount(request, uidarg=None,token=None):
 #print(force_text(urlsafe_base64_decode(uidb64)))
    #print(token)
    try:
        uid = force_text(urlsafe_base64_decode(uidarg))
        #print(type(uid),uid)
        user = User.objects.get(pk=uid)
        print(user)
    except User.DoesNotExist:
        user = None
    if user and default_token_generator.check_token(user,token):
        user.is_email_verified = True
        user.is_active = True
        user.save()
        print("Activation done")
        return render(request,'email_verification.html')
        #return Response(status=status.HTTP_200_OK,data={"details":"activated"})
    else:
        print("Activation failed")
        return render(request,'email_verification.html')
        #return Response(status=status.HTTP_403_FORBIDDEN,data={"details":"not_activated"})



def templateTest(request):
    return render(request,'email_verification.html')

@api_view(['POST'])
def emailValidator(request,email):
    try:
        validate_email(email)
        valid_email = True
    except validate_email.ValidationError:
        valid_email = False

    data = {'validate':valid_email}
    return JsonResponse(data=data)


@api_view(['GET'])
def getRandomAdvice(request):
    if request.method == 'GET':

        queryset = Advice.random(Advice.objects)
        print(queryset)

        advice_serialis = AdviceSerializer(queryset)
        return JsonResponse(advice_serialis.data,safe=False)


@api_view(['GET'])
def getAllAdvice(request):
    if request.method == 'GET':
        queryset = Advice.objects.all()
        print(queryset)

        advice_serialis = AdviceSerializer(queryset,many=True)
        return JsonResponse(advice_serialis.data,safe=False)


@api_view(['POST'])
@authentication_classes([])
@permission_classes([])
def addAdvice(request):
    if request.method == 'POST':

        Advice.objects.create(id=uuid.uuid4(),title="Read The Owner’s Manual",content="When it comes to vehicles, there is no ‘one size fits all’ prescription. For example, the oil change schedule of your car depends on the type of car that you drive. Contrary to the common misconception, you don’t need to change the oil after every 3,000 miles.",url="https://carcarehunt.com/wp-content/uploads/2019/07/owners-manual-1024x400.jpg")
        Advice.objects.create(id=uuid.uuid4(),title="Learn The Meaning Of Different Warning Light Indicators",content="Most modern cars will notify you via warning lights whenever there are some issues that require immediate attention. However, those warning lights will be useless if you can’t interpret what they mean to choose the appropriate course of action",url="https://carcarehunt.com/wp-content/uploads/2019/07/warning-lights-1024x576.jpg")
        Advice.objects.create(id=uuid.uuid4(),title="Administer The First Oil Change After 50 To 100 Miles",content="Sure, when the vehicle has been on the road for a long time, you can even change the oil after 5,000 miles. However, when a car is brand new straight out of the factory, you should change the oil after 50 or 100 miles. Later, you can change the oil after the recommended break-in period mileage.",url="https://carcarehunt.com/wp-content/uploads/2019/07/first-oil-change.jpg")
        
        

        Advice.objects.create(id=uuid.uuid4(),title="Check Tire Pressures", content="Did you know that most tire-related accidents are caused by under-inflated tires? Not only does it diminish the gas mileage and handling, but an under-inflated tire can trigger a dangerous blowout. Don’t wait for the mechanic to check your tire pressure once in a blue moon but you should do it yourself every month. As per the norm, the owner’s manual will tell you the appropriate pressure for your tires.", url="https://carcarehunt.com/wp-content/uploads/2019/07/check-tire-pressure.jpg")
        Advice.objects.create(id=uuid.uuid4(),title="Check Your Spare Tire", content="Just because you’re certain there is a spare tire in your trunk, doesn’t mean it will be useful when push comes to shove. Do you check its air pressure once in a while? Have you confirmed it won’t mismatch when you install it? The last thing you want is wasting money on a tow truck because you forgot the check the condition of your spare tire.", url="https://carcarehunt.com/wp-content/uploads/2019/07/spare-tire.jpg")
        Advice.objects.create(id=uuid.uuid4(),title="Check The Oil Regularly", content="If the engine is the heart of your car, the oil is the blood pumping in the veins that keeps it going. In other words, the oil is crucial for lubrication which helps to reduce friction. Further, the lower the friction, the less the chances of mechanical wear and tear.", url="https://carcarehunt.com/wp-content/uploads/2019/07/engine-oil-1024x680.jpg")
        Advice.objects.create(id=uuid.uuid4(),title="Keep Your Engine From Overheating", content="Now that you know when the coolant light is on, it means the engine is overheating, you should know how to prevent it. But how do you do that? Well, it’s not rocket science, just check the coolant level occasionally. If you didn’t get the memo, the coolant plays a crucial role in regulating the engine temperature. Also, check out for any symptoms of a coolant leak.", url="https://carcarehunt.com/wp-content/uploads/2019/07/coolant-light-1024x576.jpg")


        Advice.objects.create(id=uuid.uuid4(),title="Change The Air Filter At Regular Intervals",
                            content="The air filter helps to circulate air in your engine to improve power and performance. Other than that, it stops debris and dust from accumulating in your engine. Hence, changing your air filter regularly especially if you drive on dusty roads can prevent a choked up engine. Of course, you should confirm with your car manufacturer how often you should change the air filter but just for the record, it can be replaced after 15,000 to 20,000 miles.",
                            url="https://carcarehunt.com/wp-content/uploads/2019/07/engine-air-filter.jpg")
        Advice.objects.create(id=uuid.uuid4(),title="Properly Secure The Battery In The Engine Bay",
                              content="The car battery is a very fragile part of the engine; if you don’t secure it, the engine vibration can disorient the battery plates. Moreover, the vibrations can also damage the terminals prompting the car to stop. All that trouble can be avoided just by locking the battery in the engine bay.",
                              url="https://carcarehunt.com/wp-content/uploads/2019/07/car-battery.jpg")
        Advice.objects.create(id=uuid.uuid4(),title="Clean The Lights Regularly",
                              content="It’s quite common for dirt and dust to pile up on your headlights and negatively affecting the visibility. Unlike dirt on the windshield which is immediately noticed, most folks can drive for miles with no idea the low illumination is due to building up dirt. In fact, you should clean your headlights with a damp microfiber towel every week.",
                              url="https://carcarehunt.com/wp-content/uploads/2019/07/clean-car-lights.jpg")
        Advice.objects.create(id=uuid.uuid4(),title="Regularly Run The Air Conditioning System",
                              content="The A/C is like a competitive runner; if it doesn’t run regularly, it will lose its form. Hence, you should try to run the A/C for at least 10 minutes once per week. Make sure when you do it, you adjust to the coldest settings and let the fan peak. Also, regularly running the defroster is equally important for the same reason why you should consistently run the air conditioning system.",
                              url="https://carcarehunt.com/wp-content/uploads/2019/07/air-conditioning.jpg")
        Advice.objects.create(id=uuid.uuid4(),title="Get The Check Engine Light Checked",
                              content="One way to know if there is a problem with the exhaust is to check the engine light. Granted, there could be a lot of reasons why that check engine light comes on, but in most cases, it may a problem connected to the emission system. Fortunately, you can run the On-Board Diagnostics (OBD) to pinpoint issues such as failed oxygen sensors, exhaust system break down, loose gas cap, engine misfire, and bad catalytic converters.",
                              url="https://carcarehunt.com/wp-content/uploads/2019/07/check-engine-light.jpg")
        Advice.objects.create(id=uuid.uuid4(),title="Protect Your Exterior",
                              content="You see that small chip or crack on the windscreen that if it was any smaller, you would probably need a magnifying glass to view it? As it turns out, that very small chip on your windscreen can develop into a bigger crack forcing you to part with more money to fix it. In other words, if you don’t get it repaired immediately, you may regret it.",
                              url="https://carcarehunt.com/wp-content/uploads/2019/07/park-car-in-direct-sunlight.jpg")

        return Response(status=status.HTTP_201_CREATED, data=None)


def login_admin(request):
    form = LoginForm(request.POST or None)

    msg = None

    if request.method == "POST":

        if form.is_valid():
            username = form.cleaned_data.get("username")
            password = form.cleaned_data.get("password")
            user = authenticate(username=username, password=password)
            if user is not None:
                if user.is_superuser:
                    login(request, user)
                    return redirect("/serverAPI/admin_control_panel/")
                else:
                    msg = 'Invalid user'
            else:
                msg = 'Invalid credentials'
        else:
            msg = 'Error validating the form'

    return render(request, "accounts/login.html", {"form": form, "msg" : msg})


@login_required(login_url="/admin_login/")
def index(request):

    today = datetime.date.today()

    months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October',
              'November', 'December']

    date = [months[today.month-5],months[today.month-4],months[today.month-3],months[today.month-2],months[today.month-1]]

    verification_states = [0,0,0,0,0]
    reparation_states = [0,0,0,0,0]
    recommendation = [0, 0, 0, 0, 0]
    car_trade = [0,0,0]

    car_trade[0] = Trade.objects.filter(car__car_type="car").count()
    car_trade[1] = Trade.objects.filter(car__car_type="pickup").count()
    car_trade[2] = Trade.objects.filter(car__car_type="truck").count()
    if not Trade.objects.count() == 0 :
        car_trade[0] = car_trade[0]*100/Trade.objects.count()
        car_trade[1] = car_trade[1] * 100 / Trade.objects.count()
        car_trade[2] = car_trade[2] * 100 / Trade.objects.count()

    for verification in Verification.objects.all():
        verif = model_to_dict(verification)
        verif['date'] = verif['date']/1000
        verif['date'] = datetime.datetime.fromtimestamp(verif['date']).strftime('%Y-%m')

        if int(verif['date'][0:4]) == today.year:
            if int(verif['date'][5:7]) == today.month-4 :
                verification_states[0] = verification_states[0]+ 1
            elif int(verif['date'][5:7]) == today.month-3 :
                verification_states[1] = verification_states[1]+ 1
            elif int(verif['date'][5:7]) == today.month-2 :
                verification_states[2] = verification_states[2]+ 1
            elif int(verif['date'][5:7]) == today.month-1 :
                verification_states[3] = verification_states[3]+ 1
            elif int(verif['date'][5:7]) == today.month :
                verification_states[4] = verification_states[4]+ 1

    for reparation in Reparation.objects.all():
        repare = model_to_dict(reparation)
        repare['date'] = repare['date'] / 1000
        repare['date'] = datetime.datetime.fromtimestamp(repare['date']).strftime('%Y-%m')

        if int(repare['date'][0:4]) == today.year:
            if int(repare['date'][5:7]) == today.month - 4:
                reparation_states[0] = reparation_states[0] + 1
            elif int(repare['date'][5:7]) == today.month - 3:
                reparation_states[1] = reparation_states[1] + 1
            elif int(repare['date'][5:7]) == today.month - 2:
                reparation_states[2] = reparation_states[2] + 1
            elif int(repare['date'][5:7]) == today.month - 1:
                reparation_states[3] = reparation_states[3] + 1
            elif int(repare['date'][5:7]) == today.month:
                reparation_states[4] = reparation_states[4] + 1




    for recom in PredictedMaintenance.objects.all():
        rec = model_to_dict(recom)
        rec['date'] = rec['date'] / 1000
        rec['date'] = datetime.datetime.fromtimestamp(rec['date']).strftime('%Y-%m')

        if int(rec['date'][0:4]) == today.year:
            if int(rec['date'][5:7]) == today.month - 4:
                recommendation[0] = recommendation[0] + 1
            elif int(rec['date'][5:7]) == today.month - 3:
                recommendation[1] = recommendation[1] + 1
            elif int(rec['date'][5:7]) == today.month - 2:
                recommendation[2] = recommendation[2] + 1
            elif int(rec['date'][5:7]) == today.month - 1:
                recommendation[3] = recommendation[3] + 1
            elif int(rec['date'][5:7]) == today.month:
                recommendation[4] = recommendation[4] + 1


    print(verification_states)
    print(reparation_states)
    print(recommendation)
    print(car_trade)

    context = {
        'segment':'index',
        'date':date,
        'verification_states':verification_states,
        'reparation_states':reparation_states,
        'recommendation':recommendation,
        'car_trade':car_trade[0],
        'truck_trade': car_trade[2],
        'pickup_trade': car_trade[1],
        'user':User.objects.all().count(),
        'verification':Verification.objects.all().count(),
        'car':Car.objects.all().count(),
        'service_center':ServiceCenter.objects.all().count()
    }

    html_template = loader.get_template('index.html')
    return HttpResponse(html_template.render(context, request))



@login_required(login_url="/admin_login/")
def users(request):
    context = {
        'segment':'users',
        'users':User.objects.all()
    }

    html_template = loader.get_template('users.html')
    return HttpResponse(html_template.render(context, request))


@login_required(login_url="/admin_login/")
def advices(request):
    form = AdviceForm(request.POST or None)


    if request.method == "POST" and form.is_valid():
        url = form.cleaned_data.get("image_url")
        title = form.cleaned_data.get("title")
        content = form.cleaned_data.get("content")
        id = uuid.uuid4()
        Advice.objects.create(id=id,url=url,title=title,content=content)
        return redirect("/serverAPI/advices_list/")


    context = {
        "form": form,
        'segment': 'advices',
        'advices': Advice.objects.all()
    }

    html_template = loader.get_template('advices.html')
    return HttpResponse(html_template.render(context, request))

def my_car_public(request):
    context = {
    }

    html_template = loader.get_template('my_car_index.html')
    return HttpResponse(html_template.render(context, request))


def my_car_contact(request):
    context = {
    }

    html_template = loader.get_template('contact.html')
    return HttpResponse(html_template.render(context, request))



@login_required(login_url="/admin_login/")
def remove_user(request,pk):

    rem = User.objects.get(email=pk)
    if (rem is not None) and (not rem.is_superuser):
                rem.delete()
                return redirect('/serverAPI/users_list/')

    context = {
        'segment': 'users',
        'users': User.objects.all()
    }
    html_template = loader.get_template('users.html')
    return HttpResponse(html_template.render(context, request))


@api_view(['POST'])
@authentication_classes([])
@permission_classes([])
def sendNotification(request):
    device = FCMDevice.objects.all().first()
    device.send_message("Title", "Message")

    return Response(status=status.HTTP_200_OK, data=None)



class TradeViewSet(viewsets.ModelViewSet):
    queryset = Trade.objects.all()
    serializer_class = TradeSerializer
    permission_classes = [IsOwner, IsAuthenticatedOrReadOnly, ]

    def get_queryset(self):
        user = self.request.user
        if user.is_authenticated:
            queryset = Trade.objects.filter(seller=user)
            print(queryset)
            queryset2 = Trade.objects.filter(buyer=user)
            print(queryset2)
            queryset = queryset | queryset2
            return queryset

        raise PermissionDenied()

    def create(self, request):
        car = request.data.pop('car')
        carvalue = Car.objects.get(id=car['id'])
        id_user = request.data.pop('id_user_buyer')
        buyer = User.objects.get(id=id_user)
        print(buyer)
        seller = User.objects.get(id=carvalue.owner.id)
        print(seller)

        trade = Trade.objects.create(id=uuid.uuid4(), date = datetime.datetime.now().timestamp()*1000, car=carvalue,seller=seller,buyer=buyer)
        if trade.id is not None:
            Car.objects.filter(id=car['id']).update(owner=buyer)
            queryset_service = ServiceCenter.objects.filter(owner=seller)
            for service in queryset_service:
                print(ServiceCenter.objects.filter(name=service.name,owner=buyer).count())
                if ServiceCenter.objects.filter(name=service.name,owner=buyer).count() == 0:
                    service.id = uuid.uuid4()
                    service.owner=buyer
                    service.save()
                    c = Car.objects.get(id=car['id'])
                    verif = Verification.objects.filter(car=c)
                    repare = Reparation.objects.filter(car=c)
                    for v in verif:
                        if v.service_center.name == service.name:
                            Verification.objects.filter(id=v.id).update(service_center=service)
                    for r in repare:
                        if r.service_center.name == service.name:
                            Reparation.objects.filter(id=r.id).update(service_center=service)
                else:
                    c = Car.objects.get(id=car['id'])
                    s = ServiceCenter.objects.filter(name=service.name,owner=buyer).first()
                    verif = Verification.objects.filter(car=c)
                    repare = Reparation.objects.filter(car=c)
                    for v in verif:
                        if v.service_center.name == s.name:
                            Verification.objects.filter(id=v.id).update(service_center=s)
                    for r in repare:
                        if r.service_center.name == s.name:
                            Reparation.objects.filter(id=r.id).update(service_center=s)




            device = FCMDevice.objects.filter(user=buyer).first()
            device.send_message("Request for buying car", "Enter and validate buying request")
            return Response(status=status.HTTP_201_CREATED, data=request.data)
        else:
            return Response(data=None)


@api_view(['GET'])
def getBuyerKey(request):
    if request.method == 'GET':
        user = request.user
        return JsonResponse({"user_key":user.id},safe=False)


@login_required(login_url="/admin_login/")
def remove_advice(request,pk):

    rem = Advice.objects.get(id=pk)
    if rem is not None:
                rem.delete()
                return redirect('/serverAPI/advices_list/')

    context = {
        'segment': 'advices',
        'advices': Advice.objects.all()
    }
    html_template = loader.get_template('advices.html')
    return HttpResponse(html_template.render(context, request))


@api_view(['POST'])
def update_device(request,pk):
    FCMDevice.objects.filter(user=request.user).update(registration_id=pk)

    return Response(status=status.HTTP_201_CREATED, data={"state":"ok"})







