#class classe:
 #   def rabie(self):
  #      return (5)


from server.maintenance_prediction.contenent_filtering_final import Contenent_Filtering
from server.maintenance_prediction.data_preparation import Data_Preparation
from server.maintenance_prediction.maintenance_prediction import Maintenance_Prediction
from server.maintenance_prediction.user_based_final import User_Based

#creat un example of maintenance
l=[]
d={
    "EquipmentID":int(210),
    "VehicleYear":2019,
    "Make":"renault",
    "Model":"Duster",
    "VehicleType":"Car",
    "VehicleMileage":40000,
    "MaintenanceType":"Oil Change"
}
l.append(d)
d1=d.copy()
d={}
d=d1
d["VehicleMileage"]=100000
d["MaintenanceType"]="Brake Job"
l.append(d)
d1=d.copy()
d={}
d=d1
d["VehicleMileage"]=150000
d["MaintenanceType"]="Tire Replacement"
l.append(d)
#l is the list of dictionary
Data_Preparation().data_preparation(l)
User_Based().user_baesd()
Contenent_Filtering().contenent_filtering()
#k is the list of tuplet (maintenance, coefficiant) of maintenance recomondation
k=Maintenance_Prediction().maintenance_prediction()

