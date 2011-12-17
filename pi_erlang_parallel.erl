%  Calculation of Pi using quadrature. Parallel algorithm.
%
%  Copyright © 2008–2009,2011 Russel Winder

-module ( pi_erlang_parallel ) .
-export ( [ start / 0 ] ) .
-import ( microsecondTime , [ microsecondTime / 0 ] ) .

pi_iter ( _To , _From , _To , Delta , Sum ) -> 4.0 * Delta * Sum ;
pi_iter (  I , From , To , Delta , Sum ) ->
	X = ( I - 0.5 ) * Delta ,
	pi_iter ( I + 1 , From , To , Delta , Sum + 1.0 / ( 1.0 + X * X ) ) .

pi_iter ( From , To , Delta ) ->
    pi_iter ( From , From , To , Delta , 0.0 ) .

pi ( N , NWorkers ) ->
    NPerWorker = trunc ( N / NWorkers + 0.5 ) ,
    Delta = 1 / N ,
    Self = self ( ) ,
    Pids = [ spawn ( fun ( ) -> Self ! pi_iter ( I , lists:min ( [ N , I + NPerWorker - 1 ] ) , Delta ) end )  || I <- lists:seq ( 1 , N , NPerWorker ) ],
    Results = [ receive R -> R end || _ <- Pids ] ,
    lists:sum ( Results ) .

execute ( NWorkers ) ->
    N = 100000000 , % 10 times fewer due to speed issues.
    StartTime = microsecondTime ( ) ,
    Pi = pi ( N , NWorkers ) ,
    ElapseTime = microsecondTime ( ) - StartTime ,
    io:format ( "==== Erlang Parallel pi = ~p~n" , [ Pi ] ) ,
    io:format ( "==== Erlang Parallel iteration count = ~p~n" , [ N ] ) ,
    io:format ( "==== Erlang Parallel elapse = ~p~n" , [ ElapseTime ] ) ,
    io:format ( "==== Erlang Parallel scheduler count = ~p~n" , [ erlang:system_info ( schedulers ) ] ) ,
    io:format ( "==== Erlang Parallel worker count = ~p~n" , [ NWorkers ] ) .

start ( ) ->
    execute ( 1 ) ,
    io:format ( "~n" ) ,
    execute ( 2 ) ,
    io:format ( "~n" ) ,
    execute ( 8 ) ,
    io:format ( "~n" ) ,
    execute ( 32 ) ,
    halt ( ) .
