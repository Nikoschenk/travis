% File: principles.pl



% ---------------------------
% Principles
% ---------------------------


% Head-Feature Principle

phrase *> (sign,
           syn:pos:X,
           head_dtr:syn:pos:X).
           
(word,
 syn:pos:(v,
          vform:fin)) *> (syn:subj:[(pos:case:nom)]).
           
           
