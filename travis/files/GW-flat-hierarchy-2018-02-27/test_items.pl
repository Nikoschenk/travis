% File: test_items.pl

% Format for test items:
%
% t(Item-number,"word word ...", Num-of-solutions).  or
% t(Item-number,"word word ...", Description, Num-of-solutions,Comment-string).




t(1,"lilly smokes",phrase,1,'').
t(2,"she danced",phrase,1,'').
t(3,"Fido likes Lilly",phrase,1,'').
t(4,"Fido likes her",phrase,1,'').
t(5,"he likes her",phrase,1,'').
t(6,"it smokes",phrase,1,'').
t(7,"he likes it",phrase,1,'').
t(8,"he likes",phrase,0,'').
t(9,"he likes her",phrase,1,'').
t(10,"likes her",phrase,1,'').











