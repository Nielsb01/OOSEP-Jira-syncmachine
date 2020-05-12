//package nl.avisi.timer;
//
//import nl.avisi.propertyreaders.JiraSynchronisationProperties;
//
//import javax.ejb.Startup;
//import javax.inject.Inject;
//
//public class SynchronisationTimerStarter {
//
//    JiraSynchronisationProperties synchronisationProperties;
//
//    @Inject
//    public void setSynchronisationProperties(JiraSynchronisationProperties synchronisationProperties) {
//        this.synchronisationProperties = synchronisationProperties;
//    }
//
//    public SynchronisationTimerStarter() {
//        long delay = 1000 * 10;//60 * 60 * 24 * 7;
//        startSynchronisationTimer(delay);
//    }
//
//    public void startSynchronisationTimer(long delay) {
//        EventTimer synchronisationTimer = new EventTimer(
//                new SynchroniseTask(),
//                synchronisationProperties.getSynchronisationMoment(),
//                delay);
//    }
//}
