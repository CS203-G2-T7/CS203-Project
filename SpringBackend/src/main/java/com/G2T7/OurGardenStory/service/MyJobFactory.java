// package com.G2T7.OurGardenStory.service;

// import javax.inject.Inject;

// import org.quartz.Job;
// import org.quartz.Scheduler;
// import org.quartz.SchedulerException;
// import org.quartz.simpl.SimpleJobFactory;
// import org.quartz.spi.TriggerFiredBundle;

// import com.G2T7.OurGardenStory.geocoder.AlgorithmServiceImpl;

// public class MyJobFactory extends SimpleJobFactory {
//     private final RelationshipService relationshipService;
//     private final MailService mailService;
//     private final AlgorithmServiceImpl algorithmService;
//     private final UserService userService;

//     @Inject
//     public MyJobFactory(RelationshipService relationshipService, MailService mailService, AlgorithmServiceImpl algorithmService, UserService userService) {
//         this.relationshipService = relationshipService;
//         this.mailService = mailService;
//         this.algorithmService = algorithmService;
//         this.userService = userService;
//     }

//     @Override
//     public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
//         final Job job = super.newJob(bundle, scheduler);
//         if (job instanceof BallotService) {
//             relationshipService.inject((BallotService) job);
//             mailService.inject((BallotService) job);
//             algorithmService.inject((BallotService) job);
//             userService.inject((BallotService) job);
//         }
//         return job;
//     }
// }
