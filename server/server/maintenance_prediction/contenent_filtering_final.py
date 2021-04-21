import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.metrics import pairwise_distances
from sklearn.feature_extraction.text import CountVectorizer

class Contenent_Filtering:
    def contenent_filtering(self):
        vehicles_maitenance = pd.read_csv("server/maintenance_prediction/maitenance2_sort.csv")

        data= pd.read_csv("server/maintenance_prediction/data.csv")

        vehicles_maitenance = vehicles_maitenance.drop(["DatePurchased","DateOfMaintenance"], axis=1)

        vehicles_maitenance=vehicles_maitenance.append(data)

        vehicles_maitenance['allMaintenance']=''

        #sum all the maintenance in one column(allMaintenance)
        y=0
        for i in vehicles_maitenance.EquipmentID:
            l=list(vehicles_maitenance[vehicles_maitenance.EquipmentID==i].MaintenanceType)
            l = list(set(l))
            s=''
            s1=''
            for x in l:
                s1=str(x)
                s1=s1.replace(" ", "")
                s=s+s1
            vehicles_maitenance['allMaintenance'][vehicles_maitenance.EquipmentID==i]=s
            y=y+1

        vehicles_maitenance1 = vehicles_maitenance.drop(["MaintenanceType"], axis=1)

        df=vehicles_maitenance1.drop_duplicates(subset=['EquipmentID'], keep='first')

        df['soupe']=""

        #sum all the caracteristic of the caa in one column(soupe)
        s=""
        for i in df.EquipmentID:
            s=""
            s=str(df[df.EquipmentID==i].VehicleYear.values)+" "+str(df[df.EquipmentID==i].Make.values)+" "+str(df[df.EquipmentID==i].Model.values)+" "+str(df[df.EquipmentID==i].VehicleType.values)+" "+str(df[df.EquipmentID==i].allMaintenance.values)
            s=s.replace("[","")
            s=s.replace("]","")
            s=s.replace(".","")
            df['soupe'][df.EquipmentID==i]=s

        df.set_index('EquipmentID', inplace=True, drop=True)

        count = CountVectorizer(stop_words='english')
        count_matrix = count.fit_transform(df['soupe'])

        cosine_sim2 = cosine_similarity(count_matrix, count_matrix)

        np.fill_diagonal(cosine_sim2, 0 )
        similarity_with_vehicle = pd.DataFrame(cosine_sim2,index=df.index)
        similarity_with_vehicle.columns=df.index

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
        # if the number of a specific maintenance of a similair vehicle is the same or inferieur of our vehicle the this maintenance
        # will not be added to the list of maintennace
        for i in range(3):
            j = list(set(k[i]))
            h = []

            for a in j:
                if k[i].count(a) > l.count(a):
                    # l3.insert(j, i)
                    h.append(a)
            d[i + 1] = h

        #creat new datafram that contain maitenance of top 3 vehicle similair with vehicle1 and their coefficiant for exapmle the
        #maitenance of vehicle top 1 have coefficiant 3 and top2 have 2 and top 3 have ceofficant 1

        d1={
            "maitenance":[],
            "coeficiant":[]
        }
        df1=pd.DataFrame.from_dict(d1)

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

                df1.loc[i] = [d[x + 1][y], z]
                i = i + 1

        # groupe by maitenance and sum their coefficiant
        #df2=df1.groupby(['maitenance']).sum()
        df2=df1.groupby(['maitenance'], as_index=False).sum()

        #sort by coefficant
        df2=df2.sort_values(by=['coeficiant'],ascending=False)

        df1.to_csv("server/maintenance_prediction/contenent_filtering_result.csv",index=False)
