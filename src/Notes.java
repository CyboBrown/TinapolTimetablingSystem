public @interface Notes {
    // Utilizes a priority-based vertical stacking algorithm... char
    // TODO: align all meetings in a week or prioritize weekly_meetings as well
    // TODO: cleanup code afterwards
    // TODO: add more code explainer comments
    // TODO: implement requested constraints
    // TODO: implement section to activity to section distribution
    // TODO: create a manually customizable version of timetable

    // ABANDONED CODES:

//                if(c.number_of_compatible_rooms == 1) {
//                    for(Room r : rooms) {
//                        if(r.type == c.compatible_rooms.get(0)) {
//                            for(int i = 0; i < one_per_week_priority.length; i++) {
////                                if(one_per_week_priority[i] == 6 && !Timetable.include_saturday) { // skip saturday
////                                    continue;
////                                }
//                                DaySched sched = r.scheds.get(Timetable. one_per_week_priority[i] - 1);
//                                while(classes_offered != 0) {
//                                    boolean success = sched.addActivity(c, c.minutes, instance);
//                                    if(success) {
//                                        classes_offered--;
//                                        instance++;
//                                    } else {
//                                        break;
//                                    }
//                                }
//                                if(classes_offered == 0) break;
//                            }
//                        }
//                        if(classes_offered == 0) break;
//                    }
//                } else {
//                    for(int i = 0; i < c.number_of_compatible_rooms; i++) {
//                        for(Room r : rooms) {
//                            if(r.type == c.compatible_rooms.get(i)) {
//                                for(int j = 0; j < one_per_week_priority.length; j++) {
//                                    DaySched sched = r.scheds.get(Timetable. one_per_week_priority[j] - 1);
//                                    while(classes_offered != 0) {
//                                        boolean success = sched.addActivity(c, c.minutes, instance);
//                                        if(success) {
//                                            classes_offered--;
//                                            instance++;
//                                        } else {
//                                            break;
//                                        }
//                                    }
//                                    if(classes_offered == 0) break;
//                                }
//                            }
//                            if(classes_offered == 0) break;
//                        }
//                        if(classes_offered == 0) break;
//                    }
//                }

//        int temp_current_vacant = current_vacant;
//        for(int i = temp_current_vacant; i < temp_current_vacant + duration; i += Timetable.interval) {
//            if(Timetable.excluded_periods.contains(i)) {
//                temp_current_vacant = i + Timetable.interval;
//                i = temp_current_vacant;
//                continue;
//            }
//            if(i >= period_end) {
//                return false;
//            }
//        }

//        for(int i = current_vacant; i < current_vacant + duration; i += Timetable.interval) {
//            if(Timetable.excluded_periods.contains(i)) { // Jumps to next interval if period is excluded
//                current_vacant = i + Timetable.interval;
//                i = current_vacant;
//                continue;
//            }
//            if(i >= period_end) {
//                return false;
//            }
//        }

//                            for(Activity a : sched1.activities) {
//                                if(
//                                        a.instructor == null &&
//                                        t_sched1.checkVacant(a.start_time, a.duration) &&
//                                        a.course == current_course &&
//                                        t.addMinutes(a.duration)
//                                ) {
//                                    a.instructor = t;
//                                    t_sched1.addExistingActivity(a);
//                                }
//                            }
//                            for(Activity a : sched2.activities) {
//                                if(
//                                        a.instructor == null &&
//                                        t_sched2.checkVacant(a.start_time, a.duration) &&
//                                        a.course == current_course &&
//                                        t.addMinutes(a.duration)
//                                ) {
//                                    a.instructor = t;
//                                    t_sched2.addExistingActivity(a);
//                                }
//                            }

//    private void checkTimetableHealth() {
//        for(Course c : courses) {
//            int roomless_classes = c.classes_offered - c.course_classes.get(c.course_classes.size() - 1).instance;
//            if(roomless_classes == 0) {
//                System.out.println("All " + c.name + " class(es) have rooms");
//            } else {
//                System.err.println(roomless_classes + " " + c.name + " class(es) have no rooms");
//            }
//        }
//        for(Instructor i : instructors) {
//            System.out.println(i.name);
//        }
//    }

//            switch(c.lecture_component.weekly_meetings) {
//                case 1:
//                    for(int i = 0; i < c.lecture_component.number_of_compatible_rooms; i++) {
//                        for(Room r : rooms) {
//                            if(r.type == c.lecture_component.compatible_rooms.get(i)) { // Loops through all compatible rooms based on the order of the course's compatible_rooms list
//                                for(int j = 0; j < one_per_week_priority.length; j++) { // Loops through all available days based on priority and once the priority date is filled, it will proceed to the next priority
//                                    if(!include_saturday && one_per_week_priority[j] == 6) { // Skips saturday if include_saturday is disabled
//                                        continue;
//                                    }
//                                    DaySched sched = r.scheds.get(Timetable. one_per_week_priority[j] - 1); // -1 because Sunday was not included in the scheds array of Room
//                                    while(classes_offered != 0) { // Add classes of the same course to the rooms until all classes have been added to the schedule
//                                        boolean success = sched.checkViolation(r, c.minutes) && sched.addActivity(c, c.minutes, instance); // Checks if it violates the maximum minutes per day before adding activity
//                                        if(success) {
//                                            classes_offered--;
//                                            instance++;
//                                        } else { // Runs the following code if adding of classes is unsuccessful
//                                            break;
//                                        }
//                                    }
//                                    if(classes_offered == 0) break; // Proceeds to next priority day if day is full
//                                }
//                            }
//                            if(classes_offered == 0) break; // Proceeds to next priority room of the same type if room is full
//                        }
//                        if(classes_offered == 0) break; // Proceeds to the next priority type of rooms if the current type of rooms are all occupied
//                    }
//                    break;
//                case 2:
//                    int duration = c.minutes / 2; // Divides the total hours per week to the number of meetings per week
//                    for(int i = 0; i < c.number_of_compatible_rooms; i++) {
//                        for(Room r : rooms) {
//                            if(r.type == c.compatible_rooms.get(i)) {
//                                for(int j = 0; j < two_per_week_priority.length; j++) {
//                                    if(!include_saturday && (two_per_week_priority[j][0] == 6 || two_per_week_priority[j][1] == 6)) {
//                                        continue;
//                                    }
//                                    DaySched sched1 = r.scheds.get(Timetable.two_per_week_priority[j][0] - 1);
//                                    DaySched sched2 = r.scheds.get(Timetable.two_per_week_priority[j][1] - 1);
//                                    while(classes_offered != 0) {
//                                        boolean success1 = sched1.checkConflict(duration) && sched1.checkViolation(r, duration); // Check conflicts before adding to make sure that all meetings in a particular class of the course are added
//                                        boolean success2 = sched2.checkConflict(duration) && sched2.checkViolation(r, duration); // Also checks if it violates the maximum minutes per day
//                                        if(success1 & success2) {
//                                            sched1.addActivity(c, duration, instance);
//                                            sched2.addActivity(c, duration, instance);
//                                            classes_offered--;
//                                            instance++;
//                                        } else {
//                                            break;
//                                        }
//                                    }
//                                    if(classes_offered == 0) break;
//                                }
//                            }
//                            if(classes_offered == 0) break;
//                        }
//                        if(classes_offered == 0) break;
//                    }
//                    break;
//                case 3:
//                    duration = c.minutes / 3;
//                    for(int i = 0; i < c.number_of_compatible_rooms; i++) {
//                        for(Room r : rooms) {
//                            if(r.type == c.compatible_rooms.get(i)) {
//                                for(int j = 0; j < three_per_week_priority.length; j++) {
//                                    if(!include_saturday && (three_per_week_priority[j][0] == 6 || three_per_week_priority[j][1] == 6 || three_per_week_priority[j][2] == 6)) {
//                                        continue;
//                                    }
//                                    DaySched sched1 = r.scheds.get(Timetable.three_per_week_priority[j][0] - 1);
//                                    DaySched sched2 = r.scheds.get(Timetable.three_per_week_priority[j][1] - 1);
//                                    DaySched sched3 = r.scheds.get(Timetable.three_per_week_priority[j][2] - 1);
//                                    while(classes_offered != 0) {
//                                        boolean success1 = sched1.checkConflict(duration) && sched1.checkViolation(r, duration);
//                                        boolean success2 = sched2.checkConflict(duration) && sched2.checkViolation(r, duration);
//                                        boolean success3 = sched3.checkConflict(duration) && sched3.checkViolation(r, duration);
//                                        if(success1 & success2 & success3) {
//                                            sched1.addActivity(c, duration, instance);
//                                            sched2.addActivity(c, duration, instance);
//                                            sched3.addActivity(c, duration, instance);
//                                            classes_offered--;
//                                            instance++;
//                                        } else {
//                                            break;
//                                        }
//                                    }
//                                    if(classes_offered == 0) break;
//                                }
//                            }
//                            if(classes_offered == 0) break;
//                        }
//                        if(classes_offered == 0) break;
//                    }
//                    break;
//                default:
//                    System.err.println("Error: Invalid weekly_meeting value for course distribution!");
//                    break;
//            }
}
