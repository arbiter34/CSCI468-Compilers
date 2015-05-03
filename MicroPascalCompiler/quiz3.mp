program test;
   var
	a, b : Integer;
	g : Float;
	
	procedure tryit(x : Integer; g : Float);
		var
		a : Integer;
		begin
			Read(a);
					b := a + b * x;
        	        g := b * g
	        end;
    begin
    	Read(a);
    	Read(b);
    	Read(g);
    	tryit(a, g);
    	Write(a);
    	Write(b);
    	Write(g);
   end.