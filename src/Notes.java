public @interface Notes {
    // Utilizes a priority-based vertical stacking algorithm... char
    // TODO: align all meetings in a week or prioritize weekly_meetings as well
    // TODO: cleanup code afterwards

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
}
