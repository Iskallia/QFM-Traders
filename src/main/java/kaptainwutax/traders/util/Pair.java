package kaptainwutax.traders.util;

public class Pair<A, B> {
	
    private A a;
    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A first() {
        return this.a;
    }

    public B second() {
        return this.b;
    }
    
    @Override
    public boolean equals(Object obj) {
		if(obj == null)return false;
		else if(obj == this)return true;
		else if(this.getClass() != obj.getClass())return false;
		
		Pair pair = (Pair)obj;
		return pair.first().equals(this.first()) && pair.second().equals(this.second());
    }
    
    @Override
    public int hashCode() {
    	return this.a.hashCode() * 31 + this.b.hashCode();
    }
    
}
