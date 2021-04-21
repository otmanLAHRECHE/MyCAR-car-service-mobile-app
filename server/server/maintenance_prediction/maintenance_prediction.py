import pandas as pd
class Maintenance_Prediction:
    def maintenance_prediction(self):
        user_based_result = pd.read_csv("server/maintenance_prediction/user_based_result.csv")

        contenent_filtering_result = pd.read_csv("server/maintenance_prediction/contenent_filtering_result.csv")

        df=contenent_filtering_result.append(user_based_result)

        #df2=df.groupby(['maitenance']).sum()
        df2=df.groupby(['maitenance'], as_index=False).sum()

        df2=df2.sort_values(by=['coeficiant'],ascending=False)

        df2.to_csv("final_result.csv",index=False)

        index = df2.index
        number_of_rows = len(index)

        # a list of tuplet that contain all the maintenance recomendation
        t=()
        l=[]
        for i in range(number_of_rows):
            t=()
            t=(df2.iloc[i].maitenance,df2.iloc[i].coeficiant)
            l.append(t)

        return(l)