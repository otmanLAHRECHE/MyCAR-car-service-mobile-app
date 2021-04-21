import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.metrics import pairwise_distances
class User_Based:
    def user_baesd(self):
        vehicles_maitenance = pd.read_csv("server/maintenance_prediction/maitenance2_sort.csv")


        data= pd.read_csv("server/maintenance_prediction/data.csv")

        vehicles_maitenance = vehicles_maitenance.drop(["DatePurchased","DateOfMaintenance"], axis=1)

        vehicles_maitenance=vehicles_maitenance.append(data)

        final=pd.pivot_table(vehicles_maitenance,values='VehicleMileage',index='EquipmentID',columns='MaintenanceType')

        # Replacing NaN by vehicle Average mileage
        final_vehicle = final.apply(lambda row: row.fillna(row.mean()), axis=1)

        # vehicle similarity with each others
        b = cosine_similarity(final_vehicle)
        np.fill_diagonal(b, 0 )
        similarity_with_vehicle = pd.DataFrame(b,index=final_vehicle.index)
        similarity_with_vehicle.columns=final_vehicle.index

        #fuction that creat a matrix with top similarities for each user
        def find_n_neighbours(df,n):
            order = np.argsort(df.values, axis=1)[:, :n]
            df = df.apply(lambda x: pd.Series(x.sort_values(ascending=False)
                   .iloc[:n].index,
                  index=['top{}'.format(i) for i in range(1, n+1)]), axis=1)
            return df


        # top 3 neighbours for each vehicle
        sim_vehicle_3 = find_n_neighbours(similarity_with_vehicle,3)

        #recover our vehicle id
        x=int(data.loc[0].EquipmentID)

        # creat a dictionairy with top 3 vehicle and their maitenance similar with the vehicle with EquipmentID==1
        vehicle1 = sim_vehicle_3.loc[x].top1
        vehicle2 = sim_vehicle_3.loc[x].top2
        vehicle3 = sim_vehicle_3.loc[x].top3
        d = {
            1: "",
            2: "",
            3: ""
        }
        l = list(list(vehicles_maitenance[vehicles_maitenance.EquipmentID == x].MaintenanceType))
        l1 = list(vehicles_maitenance[vehicles_maitenance.EquipmentID == vehicle1].MaintenanceType)
        l2 = list(vehicles_maitenance[vehicles_maitenance.EquipmentID == vehicle2].MaintenanceType)
        l3 = list(vehicles_maitenance[vehicles_maitenance.EquipmentID == vehicle3].MaintenanceType)

        k = []
        k.append(l1)
        k.append(l2)
        k.append(l3)
        # if the number of a specific maintenance of a similair vehicle is the same or less than our vehicle then this maintenance
        # will not be added to the list of maintenance
        for i in range(3):
            j = list(set(k[i]))
            h = []

            for a in j:
                if k[i].count(a) > l.count(a):
                    # l3.insert(j, i)
                    h.append(a)
            d[i + 1] = h

        #creat new DataFrame that contain maintenance of top 3 vehicle similar with vehicle1 and their coefficiant for example the
        #maitenance of vehicle top 1 have coefficiant 3 and top2 have 2 and top 3 have ceofficant 1

        d1={
            "maitenance":[],
            "coeficiant":[]
        }
        df=pd.DataFrame.from_dict(d1)

        #convert the result to a datafram
        i = 0
        z = 0
        for x in range(len(d)):
            for y in range(len(d[x + 1])):
                # df.append({"maitenance":[d[x+1][y]],
                # "coeficiant":[x+1]},ignore_index=False")
                dd = {
                    "maitenance": d[x + 1][y],
                    "coeficiant": x + 1
                }

                if x == 0:
                    z = 3
                if x == 1:
                    z = 2
                if x == 2:
                    z = 1

                df.loc[i] = [d[x + 1][y], z]
                i = i + 1

        # group by maintenance and sum their coefficiant
        df2=df.groupby(['maitenance']).sum()

        #sort by coefficant
        df2=df2.sort_values(by=['coeficiant'],ascending=False)

        df.to_csv("server/maintenance_prediction/user_based_result.csv",index=False)








