% This script is used to plot the data against each other.

figure;
hold all;

load Results_300clients\StarterIOMultiGlobalLock.txt;
load Results_300clients\StarterIOMultiKeysLock.txt;

load Results_300clients\StarterIOMultiMapsLock.txt;
load Results_300clients\StarterIOMultiPipelinedGlobalLock.txt;

load Results_300clients\StarterIOMultiPipelinedKeysLock.txt;
load Results_300clients\StarterIOMultiPipelinedMapsLock.txt;

load Results_300clients\StarterIOSingle.txt;
load Results_300clients\StarterIOSinglePipelined.txt;

load Results_300clients\StarterNIOMultiGlobalLock.txt;
load Results_300clients\StarterNIOMultiKeysLock.txt;

load Results_300clients\StarterNIOMultiMapsLock.txt;
load Results_300clients\StarterNIOSingle.txt;

matrix = [StarterIOMultiGlobalLock, StarterIOMultiKeysLock, StarterIOMultiMapsLock, StarterIOMultiPipelinedGlobalLock, StarterIOMultiPipelinedKeysLock, StarterIOMultiPipelinedMapsLock, StarterIOSingle, StarterIOSinglePipelined, StarterNIOMultiGlobalLock, StarterNIOMultiKeysLock, StarterNIOMultiMapsLock, StarterNIOSingle];
boxplot(matrix, 1:1:12);

set(gca,'xtick',1:1:12);
set(gca,'xTickLabel',{'IOMultiGlobalLock','IOMultiKeysLock','IOMultiMapsLock','IOMultiPipelinedGlobalLock','IOMultiPipelinedKeysLock','IOMultiPipelinedMapsLock','IOSingle','IOSinglePipelined','NIOMultiGlobalLock','NIOMultiKeysLock','NIOMultiMapsLock','NIOSingle'})
set(gca, 'XTickLabelRotation', 45)
title('300 clients')
ylabel('Average waiting time (microseconds)');
xlim([0 12.5])
%set(gcf,'PaperUnits','inches','PaperPosition',[0 0 16 12])
%print('box_300clients','-dpng','-r0')