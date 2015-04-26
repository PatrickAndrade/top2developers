load resultsIOSingle.txt;
load resultsIOMultiPipelinedKeysLock.txt;

x=1:1:100; %range from 1 to 100 with step 1
y=resultsIOSingle;
z=resultsIOMultiPipelinedKeysLock;

figure;
hold all;
plot(x,y);
plot(x,z);

xlabel('Clients');
ylabel('Average serving time (microseconds)');
legend('Single threaded IO Server','Multi threaded IO Server with keys lock')
grid on;
zoom on;