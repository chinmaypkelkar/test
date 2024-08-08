 public List<String[]> fetchAvailableSlots(Integer duration, List<String[]> events) {

        if(events == null || events.isEmpty() || duration <= 0){
            return null;
        }

        List<String[]> output = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        // Set the date and time values
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.JUNE); // Note: Month is 0-based, so June is 5
        calendar.set(Calendar.DAY_OF_MONTH, 20);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);



        List<String[]> availableSlotsInDay = new ArrayList<>();
        int i = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mma"); // needs this to convert available slots in the same format as input
        final int numberOfMinutesInDay = 24*60;
        while(i < numberOfMinutesInDay){
            String[] pair = new String[2];
            pair[0] = sdf.format(calendar.getTime());
            calendar.add(Calendar.MINUTE, duration);
            pair[1] =  sdf.format(calendar.getTime());
            availableSlotsInDay.add(pair);
            i = i + duration;
        }
        for(String[] availableSlot : availableSlotsInDay) {
            boolean overLapping = false;
            for(String[] event : events) {
                if (isOverlapping(availableSlot[0], availableSlot[1], event[0], event[1])) {
                    overLapping = true;
                    break;
                }
            }
            if(!overLapping) {
                output.add(availableSlot);
            }
        }
        return output;
    }

    private boolean isOverlapping(String availableStartDateTime, String availableEndDateTime, String eventStartDateTime, String eventEndDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");
        
        // core logic - when booked slot's start time is before available slot's end time and available slot's start time is before booked slot's end time, 
        // it is overlapping and we should skip it
        return LocalDateTime.parse(eventStartDateTime, formatter).isBefore(LocalDateTime.parse(availableEndDateTime,formatter))
                && LocalDateTime.parse(availableStartDateTime, formatter).isBefore(LocalDateTime.parse(eventEndDateTime,formatter));
    }
