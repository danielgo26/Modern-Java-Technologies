package com.company;

public class Main {

    public static void main(String[] args) {
        int[][] courses = {
                {9, 11},
                {10, 12},
                {11, 13},
                {15,16},
        };

        int[][] courses2 = {
                {19, 22}, {17, 19}, {9, 12}, {9, 11}, {15, 17}, {15, 17}
        };

        int [][] courses3= {
                {13, 15}, {13, 17}, {11, 17}
        };

        int [][] courses4= {
                {12, 13}, {10,12}, {10,14}, {9,11}, {12,15}, {12,14}
        };

        CourseScheduler ch = new CourseScheduler();
        System.out.println(ch.maxNonOverlappingCourses(courses2));
    }
}
