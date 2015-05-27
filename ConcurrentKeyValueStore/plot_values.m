% This script is used to plot the data against each other.
x=1:1:500; %range from 1 to 100 with step 1

figure;
hold all;

load Results\StarterIOMultiGlobalLock.txt;
load Results\StarterIOMultiKeysLock.txt;
plot(x,StarterIOMultiGlobalLock);
plot(x,StarterIOMultiKeysLock);

load Results\StarterIOMultiMapsLock.txt;
load Results\StarterIOMultiPipelinedGlobalLock.txt;
plot(x,StarterIOMultiMapsLock);
plot(x,StarterIOMultiPipelinedGlobalLock);

load Results\StarterIOMultiPipelinedKeysLock.txt;
load Results\StarterIOMultiPipelinedMapsLock.txt;
plot(x,StarterIOMultiPipelinedKeysLock);
plot(x,StarterIOMultiPipelinedMapsLock);

load Results\StarterIOSingle.txt;
load Results\StarterIOSinglePipelined.txt;
plot(x,StarterIOSingle);
plot(x,StarterIOSinglePipelined);

load Results\StarterNIOMultiGlobalLock.txt;
load Results\StarterNIOMultiKeysLock.txt;
plot(x,StarterNIOMultiGlobalLock);
plot(x,StarterNIOMultiKeysLock);

load Results\StarterNIOMultiMapsLock.txt;
load Results\StarterNIOSingle.txt;
plot(x,StarterNIOMultiMapsLock);
plot(x,StarterNIOSingle);

xlabel('Clients');
ylabel('Average waiting time (microseconds)');
legend('IOMultiGlobalLock','IOMultiKeysLock','IOMultiMapsLock','IOMultiPipelinedGlobalLock','IOMultiPipelinedKeysLock','IOMultiPipelinedMapsLock','IOSingle','IOSinglePipelined','NIOMultiGlobalLock','NIOMultiKeysLock','NIOMultiMapsLock','NIOSingle')
grid on;
zoom on;

%set(gcf,'PaperUnits','inches','PaperPosition',[0 0 16 12])
%print('1toN','-dpng','-r0')