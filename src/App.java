public class App {
    public static void main(String[] args) throws Exception {
        Mesa m = new Mesa(5);
        for (int i = 1; i <= 5; i++) {
            Filosofo f = new Filosofo(m, i);
            f.start();
        }
    }
}


class Mesa {
     
    private boolean[] tenedores;
     
    public Mesa(int numTenedores){
        this.tenedores = new boolean[numTenedores];
    }
     
    public int tenedorIzquierda(int i){
        return i;
    }
     
    public int tenedorDerecha(int i){
        if(i == 0){
            return this.tenedores.length - 1;
        }else{
            return i - 1;
        }
    }
     
    public synchronized void tomarTenedores(int comensal){
         
        while(tenedores[tenedorIzquierda(comensal)] || tenedores[tenedorDerecha(comensal)]){
            try {   
                wait();
            } catch (InterruptedException ex) {
                // Logger.getLogger(Mesa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         
        tenedores[tenedorIzquierda(comensal)] = true;
        tenedores[tenedorDerecha(comensal)] = true;
    }
     
    public synchronized void dejarTenedores(int comensal){
        tenedores[tenedorIzquierda(comensal)] = false;
        tenedores[tenedorDerecha(comensal)] = false;
        notifyAll();
    }
     
}

class Filosofo extends Thread {
     
    private Mesa mesa;
    private int comensal;
    private int indiceComensal;
     
    public Filosofo(Mesa m, int comensal){
        this.mesa = m;
        this.comensal = comensal;
        this.indiceComensal = comensal - 1;
    }
     
    public void run(){
         
        while(true){
            this.pensando();
            this.mesa.tomarTenedores(this.indiceComensal);
            this.comiendo();
            System.out.println("Filosofo " + comensal +  " deja de comer, tenedores libres " + (this.mesa.tenedorIzquierda(this.indiceComensal) + 1) + ", " + (this.mesa.tenedorDerecha(this.indiceComensal) + 1) );
            this.mesa.dejarTenedores(this.indiceComensal);
        }
         
    }
     
    public void pensando(){
        
        System.out.println("Filosofo " + comensal + " esta pensando");
        try {
            sleep((long) (Math.random() * 4000));
        } catch (InterruptedException ex) { }
         
    }
     
    public void comiendo(){
        System.out.println("Filosofo " + comensal + " esta comiendo");
        try {
            sleep((long) (Math.random() * 4000));
        } catch (InterruptedException ex) { }
    }
     
}