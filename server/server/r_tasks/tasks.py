


from background_task import background

from server.maintenance_prediction.contenent_filtering_final import Contenent_Filtering
from server.maintenance_prediction.data_preparation import Data_Preparation
from server.maintenance_prediction.maintenance_prediction import Maintenance_Prediction
from server.maintenance_prediction.user_based_final import User_Based
from server.models import PredictedMaintenance, Car
import json
import datetime
import uuid

from fcm_django.models import FCMDevice



@background(schedule=10)
def start_predicting_maintenance(carvalue,engine_oil,odometer,changevalue):

    print("begin")
    l = []
    d = {
        "EquipmentID": int(210),
        "VehicleYear": carvalue['year'],
        "Make": carvalue['company'],
        "Model": carvalue['model'],
        "VehicleType": carvalue['car_type'],
        "VehicleMileage": odometer,
        "MaintenanceType": ""
    }


    if not (engine_oil['nextOdometer'] == 0):
        d1 = d.copy()
        d1["MaintenanceType"] = "Oil Change"
        l.append(d1)

    if changevalue['air_filter']:
        d1 = d.copy()
        d1['MaintenanceType'] = "Replaced Air Filter"
        l.append(d1)

    if changevalue['wheel_alignment']:
        d1 = d.copy()
        d1['MaintenanceType'] = "Wheel Alignment"
        l.append(d1)
    if changevalue['brakes']:

        d1 = d.copy()
        d1['MaintenanceType'] = "Brake Job"
        l.append(d1)
    if changevalue['transmission_oil']:

        d1 = d.copy()
        d1['MaintenanceType'] = "Transmission Service"
        l.append(d1)
    if changevalue['battery_replace']:

        d1 = d.copy()
        d1['MaintenanceType'] = "Battery Replaced"
        l.append(d1)
    if changevalue['tires_change']:

        d1 = d.copy()
        d1['MaintenanceType'] = "Tire Replaced"
        l.append(d1)
    if changevalue['fuel_filter_change']:

        d1 = d.copy()
        d1['MaintenanceType'] = "Fuel Filter Replaced"
        l.append(d1)
    if changevalue['glass_change']:

        d1 = d.copy()
        d1['MaintenanceType'] = "Glass Replacement"
        l.append(d1)
    if changevalue['mount_and_balance']:

        d1 = d.copy()
        d1["MaintenanceType"] = "Mount And Balance"
        l.append(d1)
    if changevalue['light_replace']:
        d1 = d.copy()
        d1['MaintenanceType'] = "Light Replaced"
        l.append(d1)



    print('start processing')

    Data_Preparation().data_preparation(l)


    User_Based().user_baesd()
    Contenent_Filtering().contenent_filtering()
    k = Maintenance_Prediction().maintenance_prediction()

    print(k)
    print('end processing')


    car = Car.objects.get(id = carvalue['id'])
    id_uuid = uuid.uuid4()

    print(id_uuid)

    PredictedMaintenance.objects.create(id= id_uuid,date = datetime.datetime.now().timestamp()*1000,car = car,predictedMaintenance = k)

    device = FCMDevice.objects.filter(user=car.owner).first()
    if device is not None:
        print("message sent")
        print("message sent")
        device.send_message("New maintenance recommendation arrived", "Enter and check the maintenance recommended")

    print("end with ok")