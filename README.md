# AI_Project1  

## Results  
We can note that in 500 rounds our bot, RoboJohn, wins 500/500, 100% of the matches versus sample.Corners. We can see that we dominate the Survival and Bullet categories, only encountering minor Bullet damage and miniscule ram damage.  

## Approach  
There we a handful of distinct features to our approach that allowed us to dominate Corners: centering, tracking, shooting, and strafing.   

### Centering
 – We developed a movement method that generates bearings from our robot to the center of the field and moves there.  
### Tracking
 – Once we arrived in the center of the field, we use a radar method to rotate until we have found Corners which triggered an OnScannedRobotEvent adapted from sample bot TrackFire.   
 
### Shooting
 – By acquiring Corners’ velocity, we can understand when Corners has reached its corner and only when it has reached its corner (velocity ~= 0), do we start shooting the largest bullets possible, as we know Corners won’t move from this corner.   

### Strafing
 – We line up orthogonally (90 degree angle) to Corners and every couple of ticks of the game, we change move significantly in the other direction. This allows for us to avoid the majority of Corners’ bullet fires.   

## Credits  
In addition to working off the sample robot, TrackFire, we utilized the Robocode wiki, http://robowiki.net/, to better understand bearings and game strategies and a Robocode tutorial, http://mark.random-article.com/weber/java/robocode/, to better understand movement and strafing.

