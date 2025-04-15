public class MyNotificationListenerService extends NotificationListenerService {
    private static final String CHANNEL_ID = "my_channel_id";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.getPackageName().equals("com.whatsapp")) {
            // Notification from WhatsApp received

            // Schedule a JobScheduler job
            JobScheduler jobScheduler = getSystemService(JobScheduler.class);
            JobInfo jobInfo = new JobInfo.Builder(JOB_ID, new ComponentName(this, MyJobService.class))
                    .build();
            jobScheduler.schedule(jobInfo);
        }
    }

    // ... other notification listener methods
}