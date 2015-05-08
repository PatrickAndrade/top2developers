% This script is used to plot the data against each other.
x=1:1:500; %range from 1 to 100 with step 1

figure;
hold all;

load Results\StarterIOMultiGlobalLock.txt;
load Results\StarterIOMultiKeysLock.txt;


load Results\StarterIOMultiMapsLock.txt;
load Results\StarterIOMultiPipelinedGlobalLock.txt;


load Results\StarterIOMultiPipelinedKeysLock.txt;
load Results\StarterIOMultiPipelinedMapsLock.txt;


load Results\StarterIOSingle.txt;
load Results\StarterIOSinglePipelined.txt;


load Results\StarterNIOMultiGlobalLock.txt;
load Results\StarterNIOMultiKeysLock.txt;


load Results\StarterNIOMultiMapsLock.txt;
load Results\StarterNIOSingle.txt;

matrix = [StarterNIOSingle, StarterNIOMultiMapsLock];
boxplot(matrix, [1,2]);

%print -r1600 -depsc2 part3_lambda70_withoutTransient
% set(gcf,'PaperUnits','inches','PaperPosition',[0 0 16 12])
% print('1toN','-dpng','-r0')