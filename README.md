# akka-ask-patterns

1. Import and build project akka-ask-patterns
2. Start Remote Actor using 
    apps->RemoteApp
3. Start Local Actor using
    apps->LocalApp
    
    
The following logs is from Remote Actor:
and clearly it does not have sender actor reference as it prints temp/$b

```bash
...
[INFO] [10/29/2018 07:56:54.856] [ClusterSystem-akka.actor.default-dispatcher-4] [akka://ClusterSystem/user/ActorA] I am Actor[akka://ClusterSystem/user/ActorA#699582996], To Actor[akka.tcp://ClusterSystem@localhost:58154/temp/$b], String Payload: 2
[INFO] [10/29/2018 07:56:54.927] [ClusterSystem-akka.actor.default-dispatcher-4] [akka://ClusterSystem/user/ActorA] I am Actor[akka://ClusterSystem/user/ActorA#699582996], To Actor[akka.tcp://ClusterSystem@localhost:58154/temp/$c], Object Payload: 0
...
```
