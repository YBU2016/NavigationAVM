package com.example.omur.navigationavm;

import android.content.Context;

import com.example.Database.Repositories.DistancesRepository;
import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;
import com.example.models.DistanceViewModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by Alparslan on 24.4.2016.
 *
 * This Class finds zone. We sent nearest 5 modems to this Area Class.
 * In this class, we have several important operations.
 */

public class Area
{
    private RepositoryContainer rc;
    private IRepository repository;
    private Context context;
    private Vector<DistanceViewModel> model;
    private Map<String, Integer> areaList = new HashMap<>();

    public Area(Context context) {
        this.context = context;
    }
    public String findZone(HashMap<Double, String> scanResultsMap) {
        String area = "";
        rc = RepositoryContainer.create(context);
        repository = rc.getRepository(RepositoryNames.DISTANCES);


        Iterator<Map.Entry<Double, String>> scanEntries = scanResultsMap.entrySet().iterator();
        while (scanEntries.hasNext()) {   /**Here MAC adresses which is coming from scan of WiFi is iterated.*/
            Map.Entry<Double, String> entry = scanEntries.next();  /**Here, we got MAC Adresses one by one in entry variable */

            if((model = repository.getDistanceFromBSSID(entry.getValue())) != null) {
                String modelString;
                for (int counter = 0; counter < model.size(); counter++) {
                    modelString = model.get(counter).zone;
                    /** Below if controls the distance. */
                    if (entry.getKey() <= model.get(counter).farthest && entry.getKey() >= model.get(counter).shortest) {
                        if (areaList.isEmpty() || !areaList.containsKey(model.get(counter).zone)) {
                            areaList.put(modelString, 1);
                        } else if (areaList.containsKey(model.get(counter).zone)) {
                            areaList.put(modelString, areaList.get(modelString) + 1);
                        }

                    }
                }
            }

        }//End of While

        /**
         * This part looks the areaList HashMap and finds most frequently Zone in it.
         * All of the zones in the distance are recorded to a HashMap and
         * its value icreased by 1 when the same zone is in distance.
         * So, only the actual distance will have the 3 as value.
         */
        int maxFrequently = 1;
        for(String mostFrequentlyArea : areaList.keySet())
        {
            // What if same number of Frequently occurs ?
            if(areaList.get(mostFrequentlyArea) > maxFrequently)
            {
                maxFrequently = areaList.get(mostFrequentlyArea);
                area = mostFrequentlyArea;
            }
        }

        return area;
    }
}
