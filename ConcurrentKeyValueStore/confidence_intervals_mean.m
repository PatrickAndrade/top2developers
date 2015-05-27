% This scripts assumes at least 30 experiments data.
% For now, this makes no sense since, for having confidence intervals, we
% should repeat more than once (say 100 times) the same experiment and not
% an experiment where the number of clients grows. For an experiment, we
% could fix the number of clients, say 100, and do the experiment with this
% fix number of clients. Then after doing say 50 experiments we can compute
% confidence intervals. Finally, if we do it for any kind of server, we
% will be able to compare between them (watch out ! this comparison makes
% only sense for this fix number of clients, it could be that for a

load Results_10clients\StarterIOMultiGlobalLock.txt;
load Results_10clients\StarterIOMultiKeysLock.txt;

load Results_10clients\StarterIOMultiMapsLock.txt;
load Results_10clients\StarterIOMultiPipelinedGlobalLock.txt;

load Results_10clients\StarterIOMultiPipelinedKeysLock.txt;
load Results_10clients\StarterIOMultiPipelinedMapsLock.txt;

load Results_10clients\StarterIOSingle.txt;
load Results_10clients\StarterIOSinglePipelined.txt;

load Results_10clients\StarterNIOMultiGlobalLock.txt;
load Results_10clients\StarterNIOMultiKeysLock.txt;

load Results_10clients\StarterNIOMultiMapsLock.txt;
load Results_10clients\StarterNIOSingle.txt;

figure
hold all;

% confidence level
alpha = 0.05;
N = 100;

values = StarterIOMultiGlobalLock;

m=mean(values);
plot(2, m, 'bx')
ci = norminv(1-alpha/2,0,1) * std(values)/sqrt(N); 
errorbar(2,m,ci);

set(gca, 'XTickLabelMode', 'manual', 'XTickLabel', []);
%ylim([1 2])
xlim([0.5 2.5])
%print -r1600 -depsc2 confidence_intervals
