

% --------------------------
% Functions and Relations
% --------------------------

% --------------
% append/3.
% appends two lists, returning a third as the result

fun append(+,+,-).
append(L1,L2,L3) if
   when( (L1 = (e_list;ne_list); L3 = (e_list;ne_list)),
         und_append(L1,L2,L3) ).

und_append([],L,L) if true.
und_append([H|T1],L,[H|T2]) if
     append(T1,L,T2).



