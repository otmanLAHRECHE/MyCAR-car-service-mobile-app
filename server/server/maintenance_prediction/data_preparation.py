import pandas as pd
class Data_Preparation:

    def data_preparation(self,l):
        d={
            "EquipmentID":[],
            "VehicleYear":[],
            "Make":[],
            "Model":[],
            "VehicleType":[],
            "VehicleMileage":[],
            "MaintenanceType":[]
        }
        df=pd.DataFrame.from_dict(d)
        i=0
        for x in l:
            df.loc[i]=[int(x["EquipmentID"]),x["VehicleYear"],x["Make"],x["Model"],x["VehicleType"],x["VehicleMileage"],x["MaintenanceType"]]
            i=i+1
        df.EquipmentID = df.EquipmentID.astype(int)
        df.to_csv("server/maintenance_prediction/data.csv",index=False)