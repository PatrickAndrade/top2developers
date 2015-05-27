% This scripts assumes at least 30 experiments data.
% For now, this makes no sense since, for having confidence intervals, we
% should repeat more than once (say 100 times) the same experiment and not
% an experiment where the number of clients grows. For an experiment, we
% could fix the number of clients, say 100, and do the experiment with this
% fix number of clients. Then after doing say 50 experiments we can compute
% confidence intervals. Finally, if we do it for any kind of server, we
% will be able to compare between them (watch out ! this comparison makes
% only sense for this fix number of clients, it could be that for a

load Results_25clients\StarterIOMultiGlobalLock.txt;
load Results_25clients\StarterIOMultiKeysLock.txt;

load Results_25clients\StarterIOMultiMapsLock.txt;
load Results_25clients\StarterIOMultiPipelinedGlobalLock.txt;

load Results_25clients\StarterIOMultiPipelinedKeysLock.txt;
load Results_25clients\StarterIOMultiPipelinedMapsLock.txt;

load Results_25clients\StarterIOSingle.txt;
load Results_25clients\StarterIOSinglePipelined.txt;

load Results_25clients\StarterNIOMultiGlobalLock.txt;
load Results_25clients\StarterNIOMultiKeysLock.txt;

load Results_25clients\StarterNIOMultiMapsLock.txt;
load Results_25clients\StarterNIOSingle.txt;

figure
hold all;

% confidence level
alpha = 0.05;
N = 300;

for loop=1:12

    switch loop
        case 1
            values = StarterIOMultiGlobalLock;
        case 2
            values = StarterIOMultiKeysLock;
        case 3
            values = StarterIOMultiMapsLock;
        case 4
            values = StarterIOMultiPipelinedGlobalLock;
        case 5
            values = StarterIOMultiPipelinedKeysLock;
        case 6
            values = StarterIOMultiPipelinedMapsLock;
        case 7
            values = StarterIOSingle;
        case 8
            values = StarterIOSinglePipelined;
        case 9
            values = StarterNIOMultiGlobalLock;
        case 10
            values = StarterNIOMultiKeysLock;
        case 11
            values = StarterNIOMultiMapsLock;
        case 12
            values = StarterNIOSingle;
        otherwise
    end


    m=mean(values);
    plot(loop, m, 'bx')
    ci = norminv(1-alpha/2,0,1) * std(values)/sqrt(N); 
    errorbar(loop,m,ci);
end

set(gca,'xtick',1:1:12);
set(gca,'xTickLabel',{'IOMultiGlobalLock','IOMultiKeysLock','IOMultiMapsLock','IOMultiPipelinedGlobalLock','IOMultiPipelinedKeysLock','IOMultiPipelinedMapsLock','IOSingle','IOSinglePipelined','NIOMultiGlobalLock','NIOMultiKeysLock','NIOMultiMapsLock','NIOSingle'})
set(gca, 'XTickLabelRotation', 45)
title('25 clients')
ylabel('Average waiting time (microseconds)');
xlim([0 12.5])
ylim([1200 1360])
set(gcf,'PaperUnits','inches','PaperPosition',[0 0 16 12])
print('conf_25clients_mean','-dpng','-r0')
