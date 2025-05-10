package br.com.uboard.core.model.enums;

public enum SynchronizeMilestoneFrequencyEnum {
    NONE(""),
    EVERY_30_MINUTES("0 0/30 * * * ?"),
    EVERY_HOUR("0 0 * * * ?"),
    EVERY_3_HOURS("0 0 0/3 * * ?"),
    EVERY_6_HOURS("0 0 0/6 * * ?"),
    EVERY_12_HOURS("0 0 0/12 * * ?"),
    EVERY_DAY("0 %d %d * * ?"),
    ONCE_A_WEEK("0 %d %d ? * %d");

    private final String cron;

    SynchronizeMilestoneFrequencyEnum(String cron) {
        this.cron = cron;
    }

    public String getCron() {
        return cron;
    }

    public String configureEveryDayCron(Integer minutes, Integer hours) {
        return String.format(this.cron, minutes, hours);
    }

    public String configureOncePerWeekCron(Integer minutes, Integer hours, Integer weekDay) {
        return String.format(this.cron, minutes, hours, weekDay);
    }
}
