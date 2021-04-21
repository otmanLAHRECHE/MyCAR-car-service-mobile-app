from django.urls import path, include
from rest_framework import routers
from fcm_django.api.rest_framework import FCMDeviceAuthorizedViewSet


from server import views
from server.views import CarsViewSet, ServiceCenterViewSet, StatusViewSet, ChangeViewSet, EngineOilViewSet, \
    VerificationViewSet, ReparationViewSet, PredictedMaintenanceViewSet, TradeViewSet

router = routers.DefaultRouter()
router.register('cars_me',CarsViewSet,basename="cars")
router.register('service_center_me',ServiceCenterViewSet,basename="servicecenter")
router.register('status_me',StatusViewSet,basename="status")
router.register('changes_me',ChangeViewSet,basename="change")
router.register('engine_oil_me',EngineOilViewSet,basename="engineOil")
router.register('verification_me',VerificationViewSet,basename="verifications")
router.register('reparation_me',ReparationViewSet,basename="reparations")
router.register('predicted_maintenance_me',PredictedMaintenanceViewSet,basename="predicted_maintenanc")
router.register('trade_me',TradeViewSet,basename="trades")
router.register('devices', FCMDeviceAuthorizedViewSet)

urlpatterns = [
    path('', include(router.urls)),
    path('verification_car/<str:pk>/', views.getVerificationByCar),
    path('reparation_car/<str:pk>/', views.getReparationByCar),
    path('predicted_maintenance_car/<str:pk>/', views.getPredictedMaintenanceByCar),
    path('predicted_maintenance/', views.getAllPredictedMaintenance),
    path('predicted_maintenance_last/', views.getLastPredictedMaintenance),
    path('advice_random/', views.getRandomAdvice),
    path('advices/', views.getAllAdvice),
    path('advices_add/', views.addAdvice),
    path('activate/<str:uidarg>/<str:token>/', views.activateUserAccount),
    path('auth-rest/', include('rest_framework.urls')),   # without token
    path('auth/', include('djoser.urls')),
    path('auth/', include('djoser.urls.authtoken')),
    path('auth/', include('djoser.urls.jwt')),
    path('template_test/', views.templateTest),
    path('email_validation/<str:email>/', views.emailValidator),
    path('admin_login/', views.login_admin),
    path('admin_control_panel/', views.index,name='index'),
    path('users_list/', views.users,name='users_list'),
    path('advices_list/', views.advices,name='advices_list'),
    path('my_car/', views.my_car_public, name='my_car_public'),
    path('contact/', views.my_car_contact, name='contact'),
    path('remove_user/<str:pk>/', views.remove_user),
    path('send_notification/', views.sendNotification),
    path('get_buyer_key/', views.getBuyerKey),
    path('remove_advice/<str:pk>/', views.remove_advice),
    path('update_device/<str:pk>/', views.update_device),
]