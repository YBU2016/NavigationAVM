package com.example.omur.navigationavm;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Toshıba on 5.6.2016.
 */
public class PathFinder
{



    /**
     * HERE STARTS NAVIGATION CODE
     * Running steady for these inputs
     * destinationZone = "1.2.14";
     * Zone = "1.2.4";
     *
     * destinationZone = "1.2.4";
     * Zone = "1.2.14";
     *
     * destinationZone = "1.2.13";
     * Zone = "1.2.3";
     *
     * destinationZone = "1.2.16";
     * Zone = "1.2.3";
     *
     * destinationZone = "1.2.3";
     * Zone = "1.2.13";
     *
     * destinationZone = "1.2.3";
     * Zone = "1.2.6";
     * destinationZone = "1.2.16";
     * Zone = "1.2.5";
     *
     * destinationZone = "1.1.16";
     * Zone = "1.2.5";
     *
     * destinationZone = "1.2.16";
     * Zone = "1.1.15";
     *
     * destinationZone = "1.1.15";
     * Zone = "1.2.16";
     *
     * WITH EXTRACTING ONE OF THESE SAMPLE INPUTS, YOU CAN SEE AND TRY HOW OUR PROJECT WORKS
     */




    private RepositoryContainer repositoryContainer;
    private IRepository repository;

    public List<String> findZonePath(String destinationZone, String Zone, Context context){

        List<String> PathZoneList = new ArrayList<>();
        if (destinationZone.length()>0 && Zone.length()>0 ) {
            String[] destinationZoneArray = destinationZone.split("\\.");
            String[] locationZoneArray = Zone.split("\\.");
            int destinationStoreNumber = Integer.parseInt(destinationZoneArray[2]);
            int locationStoreNumber = Integer.parseInt(locationZoneArray[2]);

            //Firstly control for floor
            if (destinationZoneArray[0].equals(locationZoneArray[0])) {
                //Secondly control for corridor
                if (destinationZoneArray[1].equals(locationZoneArray[1])) {
                    // Thirdly Control for Store Number
                    if ((destinationStoreNumber % 2 == 0 && locationStoreNumber % 2 == 0) ||
                            (destinationStoreNumber % 2 == 1 && locationStoreNumber % 2 == 1)) {
                        if (destinationStoreNumber < locationStoreNumber) {
                            for (int counter = locationStoreNumber; counter > (destinationStoreNumber - 2); counter -= 2) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((locationZoneArray[0] + "."));
                                sb.append((locationZoneArray[1] + "."));
                                sb.append(counter);
                                PathZoneList.add(sb.toString());
                            }
                        } else {
                            for (int counter = (locationStoreNumber - 2); counter < destinationStoreNumber; counter += 2) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((destinationZoneArray[0] + "."));
                                sb.append((destinationZoneArray[1] + "."));
                                sb.append((counter + 2));
                                PathZoneList.add(sb.toString());
                            }
                        }

                    } else if ((destinationStoreNumber % 2 == 0 && locationStoreNumber % 2 == 1) ||
                            (destinationStoreNumber % 2 == 1 && locationStoreNumber % 2 == 0)) {
                        // location 1.1.6 destination 1.1.1    location 1.1.2 destionation 1.1.17
                        if (destinationStoreNumber < locationStoreNumber) {
                            for (int counter = locationStoreNumber; counter > (destinationStoreNumber - 3); counter -= 2) {

                                StringBuilder sb = new StringBuilder();
                                sb.append((locationZoneArray[0] + "."));
                                sb.append((locationZoneArray[1] + "."));
                                sb.append((counter));
                                repositoryContainer = RepositoryContainer.create(context);
                                repository = repositoryContainer.getRepository(RepositoryNames.DISTANCES);
                                if (repository.isZoneInNearZones(sb.toString())) {
                                    PathZoneList.add(sb.toString());
                                } else {
                                    break;
                                }
                            }
                        } else if (locationStoreNumber < destinationStoreNumber) {
                            for (int counter = locationStoreNumber; counter < destinationStoreNumber; counter += 2) {

                                StringBuilder sb = new StringBuilder();
                                sb.append((locationZoneArray[0] + "."));
                                sb.append((locationZoneArray[1] + "."));
                                sb.append(counter);

                                repositoryContainer = RepositoryContainer.create(context);
                                repository = repositoryContainer.getRepository(RepositoryNames.DISTANCES);
                                if (repository.isZoneInNearZones(sb.toString())) {
                                    PathZoneList.add(sb.toString());
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    /**
                     * If corridor numbers are different
                     * We find 2 different path and from their FarthestDistance - ShortestDistance difference,
                     * we will find out the shorter one.
                     * This part is specialized for if location corridor is 1 and destination corridor is 2.
                     * Location 1.1.16 Destination 1.2.15
                     */
                    int firstCorridorHighLimit = 31;
                    int secondCorridorHighLimit = 24;
                    int limit = firstCorridorHighLimit;
                    int firstCorridorUnderLimit = 9;
                    int secondCorridorUnderLimit = 1;
                    int underLİmit = firstCorridorUnderLimit;
                    if (destinationZoneArray[1].equals("1") && locationZoneArray[1].equals("2")) {
                        underLİmit = secondCorridorUnderLimit;
                        limit = secondCorridorHighLimit;
                    }


                    List<String> path1 = new ArrayList<>();
                    List<String> path2 = new ArrayList<>();
                    // From left the calculation is:
                    if (locationStoreNumber % 2 == 1) {
                        for (int counter = locationStoreNumber; counter <= limit; counter += 2) {

                            StringBuilder sb = new StringBuilder();
                            sb.append((locationZoneArray[0] + "."));
                            sb.append((locationZoneArray[1] + "."));
                            sb.append(counter);
                            path1.add(sb.toString());
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("1.3.0");
                        path1.add(sb.toString());

                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("1.2.24");
                        path1.add(sb3.toString());

                        if (destinationStoreNumber % 2 == 0) {
                            for (int counter = secondCorridorHighLimit; counter >= destinationStoreNumber; counter -= 2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append((destinationZoneArray[0] + "."));
                                sb2.append((destinationZoneArray[1] + "."));
                                sb2.append(counter);
                                path1.add(sb2.toString());
                            }
                            // For testing PathZoneList = path1;
                        } else if (destinationStoreNumber % 2 == 1) {
                            for (int counter = 27; counter >= destinationStoreNumber; counter -= 2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append((destinationZoneArray[0] + "."));
                                sb2.append((destinationZoneArray[1] + "."));
                                sb2.append(counter);
                                path1.add(sb2.toString());
                            }
                            // For testing PathZoneList = path1;
                        }
                    } else if (locationStoreNumber % 2 == 0) {
                        for (int counter = locationStoreNumber; counter < limit; counter += 2) {

                            StringBuilder sb = new StringBuilder();
                            sb.append((locationZoneArray[0] + "."));
                            sb.append((locationZoneArray[1] + "."));
                            sb.append(counter);
                            path1.add(sb.toString());
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("1.3.0");
                        path1.add(sb.toString());

                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("1.2.24");
                        path1.add(sb3.toString());

                        if (destinationStoreNumber % 2 == 0) {
                            for (int counter = secondCorridorHighLimit; counter >= destinationStoreNumber; counter -= 2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append((destinationZoneArray[0] + "."));
                                sb2.append((destinationZoneArray[1] + "."));
                                sb2.append(counter);
                                path1.add(sb2.toString());
                            }
                            // For testing PathZoneList = path1;
                        } else if (destinationStoreNumber % 2 == 1) {
                            for (int counter = 27; counter >= destinationStoreNumber; counter -= 2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append((destinationZoneArray[0] + "."));
                                sb2.append((destinationZoneArray[1] + "."));
                                sb2.append(counter);
                                path1.add(sb2.toString());
                            }
                        }
                    }
                    /**
                     * Here path2 is found. After it is found, shorter one will be declared and it will be our main path
                     * destinationZone = "1.1.5";
                     * Zone = "1.2.16";
                     *
                     * destinationZone = "1.1.16";
                     * Zone = "1.2.15";
                     */

                    if (locationStoreNumber % 2 == 1) {
                        for (int counter = locationStoreNumber; counter >= underLİmit; counter -= 2) {

                            StringBuilder sb = new StringBuilder();
                            sb.append((locationZoneArray[0] + "."));
                            sb.append((locationZoneArray[1] + "."));
                            sb.append(counter);
                            path2.add(sb.toString());
                        }
                        if (destinationStoreNumber % 2 == 0) {
                            for (int counter = 0; counter <= destinationStoreNumber; counter += 2) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((destinationZoneArray[0] + "."));
                                sb.append((destinationZoneArray[1] + "."));
                                sb.append(counter);
                                path2.add(sb.toString());
                            }
                        } else if (destinationStoreNumber % 2 == 1) {
                            for (int counter = 0; counter <= destinationStoreNumber; counter += 2) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((destinationZoneArray[0] + "."));
                                sb.append((destinationZoneArray[1] + "."));
                                sb.append(counter);
                                path2.add(sb.toString());
                            }
                            // For testing PathZoneList = path2; destinationZone = "1.1.5"; Zone = "1.2.16";
                        }
                    } else if (locationStoreNumber % 2 == 0) {
                        for (int counter = locationStoreNumber; counter >= 0; counter -= 2) {

                            StringBuilder sb = new StringBuilder();
                            sb.append((locationZoneArray[0] + "."));
                            sb.append((locationZoneArray[1] + "."));
                            sb.append(counter);
                            path2.add(sb.toString());
                        }
                        if (destinationStoreNumber % 2 == 0) {
                            for (int counter = 0; counter <= destinationStoreNumber; counter += 2) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((destinationZoneArray[0] + "."));
                                sb.append((destinationZoneArray[1] + "."));
                                sb.append(counter);
                                path2.add(sb.toString());
                            }
                        } else if (destinationStoreNumber % 2 == 1) {
                            for (int counter = 0; counter <= destinationStoreNumber; counter += 2) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((destinationZoneArray[0] + "."));
                                sb.append((destinationZoneArray[1] + "."));
                                sb.append(counter);
                                path2.add(sb.toString());
                            }
                        }
                    }
                    /**
                     * This is one of the most important part of the navigation part.
                     * If user wants to change corridor, we look to number of zones which user should pass,
                     * and we show user shorter one. The least Zone number, the shorter path
                     */
                    PathZoneList = path1;
                    if (path1.size() > path2.size()) {
                        PathZoneList = path2;
                    }
                    // Log.d("Liste", path1.toString());
                    // Log.d("Liste", path2.toString());
                    // For testing PathZoneList = path1;
                }
            }

        }// End of controling Zone and Destionation Zone is null or not.

        return PathZoneList;
    }
}
