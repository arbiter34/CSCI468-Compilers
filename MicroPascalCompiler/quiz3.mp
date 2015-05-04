program test;
   var
	a, b : Integer;
	g : Float;
	
	procedure tryit(x : Integer; g : Float);
		var
		a : Integer;
		begin
			a := 10;
			b := a + b * x;
			x := a;
			g := b * g
        end;
    begin
    	a := 5;
    	b := 6;
    	g := 5.5;
    	tryit(a, g);
    	WriteLn('a = ');
    	Write(a);
    	WriteLn('b = ');
    	Write(b);
    	WriteLn('g = ');
    	Write(g);
   end.