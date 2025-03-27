package com.company;

public class CourseScheduler {
    public static int maxNonOverlappingCourses(int[][] courses)
    {
        if (courses.length == 0)
            return 0;

        //use merge sort instead
        for (int i = 0; i < courses.length-1; i++){
            for(int j = i+1; j < courses.length;j++)
            {
                if (courses[i].length != 2)
                    return 0;

                if (courses[i][1] > courses[j][1]){
                    int t1 = courses[i][0];
                    int t2 = courses[i][1];
                    courses[i][0] = courses[j][0];
                    courses[i][1] = courses[j][1];
                    courses[j][0] = t1;
                    courses[j][1] = t2;
                }
            }
        }

        int count = 1;
        int currValue = courses[0][1];
        for (int i = 1; i < courses.length;i++)
        {
            if (courses[i][0] >= currValue)
            {
                currValue = courses[i][1];
                count++;
            }
        }

        return count;
    }
}
