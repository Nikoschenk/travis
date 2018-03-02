% File: principles.pl



% ---------------------------
% Principles
% ---------------------------


% Head-Feature Principle

% Niko:
%(phrase)
% *>
%(sign,head_dtr:(syn:(pos:X)),syn:(pos:X)).

% Geht auch!
%(phrase)
% *>
%(sign,head_dtr:(syn:(pos:X))),(syn:(pos:X)).

% Geht auch. 
phrase
 *>
sign,head_dtr:(syn:(pos:X)),syn:(pos:X).



% Gert:
%phrase *> (sign,
%           syn:pos:X,
%           head_dtr:syn:pos:X).
           
(word,syn:(category,pos:(v,vform:fin))) 
 *> 
(syn:(subj:[(pos:(case:nom))])).
           
           
