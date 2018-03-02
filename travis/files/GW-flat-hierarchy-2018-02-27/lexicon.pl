% File: lexicon.pl

% ----------
% I. Lexicon
% ----------


% D. Nouns

% D.1 Proper names

lilly ---> (phon:[(a_ lilly)],
            syn:(pos:n,
                  subj:[],
                  comps:[])).
                  
fido ---> (phon:[(a_ fido)],
            syn:(pos:n,
                  subj:[],
                  comps:[])).
mary ---> (phon:[(a_ mary)],
            syn:(pos:n,
                  subj:[],
                  comps:[])).
john ---> (phon:[(a_ john)],
            syn:(pos:n,
                  subj:[],
                  comps:[])).


% D.2 Pronouns

% D.2.1 Personal pronouns

% Nominative personal pronouns

i ---> (phon:[(a_ i)],
        syn:(pos:(n,
                  case:nom),
             subj:[],
             comps:[])).
he ---> (phon:[(a_ he)],
         syn:(pos:(n,
                   case:nom),
              subj:[],
              comps:[])).
she ---> (phon:[(a_ she)],
          syn:(pos:(n,
                    case:nom),
               subj:[],
               comps:[])).

% Accusative personal pronouns

me ---> (phon:[(a_ me)],
        syn:(pos:(n,
                  case:acc),
             subj:[],
             comps:[])).

him ---> (phon:[(a_ him)],
        syn:(pos:(n,
                  case:acc),
             subj:[],
             comps:[])).
             
her ---> (phon:[(a_ her)],
        syn:(pos:(n,
                  case:acc),
             subj:[],
             comps:[])).             

% Case personal pronouns

it ---> (phon:[(a_ it)],
         syn:(pos:n,
              subj:[],
              comps:[])).

% F. Verbs



% F.2 Main verbs

admires ---> (phon:[(a_ admires)],
             syn:(pos:(v,
                       vform:fin),
                  subj:[(pos:n,
                         subj:[],
                         spr:[],
                         comps:[])],
                  comps:[(pos:(n,
                               case:acc),
                         subj:[],
                         spr:[],
                         comps:[])])). 

danced ---> (phon:[(a_ danced)],
             syn:(pos:(v,
                       vform:fin),
                  subj:[(pos:n,
                         subj:[],
                         spr:[],
                         comps:[])],
                  comps:[])).

likes ---> (phon:[(a_ likes)],
             syn:(pos:(v,
                       vform:fin),
                  subj:[(pos:n,
                         subj:[],
                         spr:[],
                         comps:[])],
                  comps:[(pos:(n,
                               case:acc),
                         subj:[],
                         spr:[],
                         comps:[])])).

smokes ---> (phon:[(a_ smokes)],
             syn:(pos:(v,
                       vform:fin),
                  subj:[(pos:n,
                         subj:[],
                         spr:[],
                         comps:[])],
                  comps:[])).










