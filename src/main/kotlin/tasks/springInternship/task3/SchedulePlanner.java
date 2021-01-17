package tasks.springInternship.task3;

import tasks.IOActor;
import tasks.StatelessIO;

public class SchedulePlanner {
    private static final int MINUTES_IN_HOUR = 60;
    private static final int MINUTES_IN_DAY = 24 * MINUTES_IN_HOUR;
    private static final int DAYS_IN_WEEK = 7;

    IOActor defaultIO = new StatelessIO();

    public static void main(String[] args) {
        StatelessIO statelessIO = new StatelessIO();
        SchedulePlanner schedulePlanner = new SchedulePlanner();
        statelessIO.writeLine(schedulePlanner.solve());
    }

    private String solve() {
        return solve(defaultIO);
    }

    public String solve(IOActor io) {
        Reminder target = readReminderInfo(io);
        int remindersCount = Integer.parseInt(io.readLine());

        int minimumKnownDelta = Integer.MAX_VALUE;
        Reminder nearestReminder = null;

        AdditionalTargetInfo targetInfo = new AdditionalTargetInfo(minutesSinceDayStart(target), minutesSinceWeekStart(target));

        for (int index = 0; index < remindersCount; index++) {
            Reminder reminder = readReminderInfo(io);
            int delta = computeDelta(target, reminder, targetInfo);
            if (delta < minimumKnownDelta) {
                minimumKnownDelta = delta;
                nearestReminder = reminder;
            }
        }

        assert nearestReminder != null;
        return nearestReminder.toString();
    }

    private static int computeDelta(Reminder target, Reminder upcoming, AdditionalTargetInfo targetInfo) {
        int upcomingMinutesFromDayStart = minutesSinceDayStart(upcoming);

        if (upcoming.dayOfWeek == 0) {
            if (targetInfo.minutesSinceDayStart <= upcomingMinutesFromDayStart)
                return upcomingMinutesFromDayStart - targetInfo.minutesSinceDayStart;
            else
                return minutesUntilDayEnd(target) + upcomingMinutesFromDayStart;
        } else {
            int upcomingMinutesFromWeekStart = minutesSinceWeekStart(upcoming);
            if (targetInfo.minutesSinceWeekStart <= upcomingMinutesFromWeekStart)
                return upcomingMinutesFromWeekStart - targetInfo.minutesSinceWeekStart;
            else
                return minutesUntilWeekEnd(target) + minutesSinceWeekStart(upcoming);
        }
    }

    private static int minutesSinceDayStart(Reminder reminder) {
        return reminder.hours * MINUTES_IN_HOUR + reminder.minutes;
    }

    private static int minutesSinceWeekStart(Reminder reminder) {
        return (reminder.dayOfWeek - 1) * MINUTES_IN_DAY + minutesSinceDayStart(reminder);
    }

    private static int minutesUntilDayEnd(Reminder reminder) {
        return MINUTES_IN_DAY - minutesSinceDayStart(reminder);
    }

    private static int minutesUntilWeekEnd(Reminder reminder) {
        return MINUTES_IN_DAY * DAYS_IN_WEEK - minutesSinceWeekStart(reminder);
    }

    private static Reminder readReminderInfo(IOActor io) {
        String[] split = io.readLine().split(" ");
        int day = Integer.parseInt(split[0]);
        int hours = Integer.parseInt(split[1]);
        int minutes = Integer.parseInt(split[2]);
        return new Reminder(day, hours, minutes);
    }
}

class Reminder {
    int dayOfWeek;
    int hours;
    int minutes;

    public Reminder(int dayOfWeek, int hours, int minutes) {
        this.dayOfWeek = dayOfWeek;
        this.hours = hours;
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return dayOfWeek + " " + hours + " " + minutes;
    }
}

class AdditionalTargetInfo {
    int minutesSinceDayStart;
    int minutesSinceWeekStart;

    public AdditionalTargetInfo(int minutesSinceDayStart, int minutesSinceWeekStart) {
        this.minutesSinceDayStart = minutesSinceDayStart;
        this.minutesSinceWeekStart = minutesSinceWeekStart;
    }
}