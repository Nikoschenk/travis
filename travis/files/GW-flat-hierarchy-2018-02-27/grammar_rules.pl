% File: grammar_rules.pl

% --------------------------
% III. Phrase Structure Rules
% --------------------------

% HEAD_SUBJECT RULE

hd_subj_rule rule
(phrase,
 phon: append(Phon1,Phon2),
 syn:(category,
      subj:[],
      spr:[],
      comps:[]),
 head_dtr:Head_Dtr,
 non_head_dtrs:[Non_Head_Dtr])
===>
  cat> (Non_Head_Dtr,
        sign,
        phon:Phon1,
        syn:Subj),
  cat> (Head_Dtr,
        sign,
        phon:Phon2,
	syn:(category,
	     pos:(v,
	          vform:fin),
             subj:[Subj],
	     spr:[],
	     comps:[])).


% HEAD_Complement RULE

hd_comp_rule rule
(phrase,
 phon: append(Phon1,Phon2),
 syn:(category,
      subj:[Subj],
      spr:[],
      comps:[]),
 head_dtr:Head_Dtr,
 non_head_dtrs:[Non_Head_Dtr])
===>
  cat> (Head_Dtr,
        word,
        phon:Phon1,
	syn:(category,
	     subj:[Subj],
	     spr:[],
	     comps:[Comp])),
  cat> (Non_Head_Dtr,
        sign,
        phon:Phon2,
        syn:Comp).

