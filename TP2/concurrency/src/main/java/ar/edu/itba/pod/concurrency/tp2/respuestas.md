# Respuestas de los ejercicios que no incluyen código

## Ejercicio 1
Siendo el código:
```
public class ConcurrentThreads {
    public static class T1 implements Runnable {
        @Override
        public void run() {
            System.out.print("A");
            System.out.print("B");
        }
    }
    public static class T2 implements Runnable {
        @Override
        public void run() {
            System.out.print("1");
            System.out.print("2");
        }
    }
    public static void main(final String[] args) {
        final ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(new T1());
        pool.execute(new T2());
    }
}
```
Los posibles resultados son "A1B2", "12AB", "AB12", "A12B" y "1AB2"

## Ejercicio 2
No es lo mismo, porque un lock es sobre la instancia del objeto, y el otro es sobre la clase del objeto en si

## Ejercicio 5

### A
Genera inconsistencias posiblemente porque se usa un objeto de un tipo primitivo, se podria usar un lock sobre la instancia
```
public class CountingFactorizer implements Servlet {
    private Long count = 0L;
    public void service(ServletRequest req, ServletResponse resp) {
        synchronized (count) {
            BigInteger i = extractFromRequest(req);
            BigInteger[] factors = factor(i);
            ++count;
            encodeIntoResponse(resp, factors);
        }
    }
    public Long getCount() {
        synchronized (count) {
            return count;
        }
    }
}
```

### B
Falta lockear al metodo para no generar mas de 1 instancia por accidente
```
public class ExpensiveObjectFactory {
    private ExpensiveObject instance = null;
    //es un singleton a todos les retorno la misma instancia.
    public ExpensiveObject getInstance() {
        if (instance == null) {
            instance = new ExpensiveObject(); //tarda mucho en construir.
        }
        return instance;
    }
}
```

### C
Podría generarse un abrazo de la muerte si se quiere transferir de A -> B y de B -> A al mismo tiempo. 

Cambiaría para bloquear primero el from y hacer el withdraw y despues lockear el to y hacer el deposit
```
public class Account {
    private double balance;
    private int id;
    public void withdraw(double amount) {
        balance -= amount;
    }
    public void deposit(double amount) {
        balance += amount;
    }
    public static void transfer(Account from, Account to, double amount) {
        synchronized (from) {
            synchronized (to) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }
}
```

### D
Está bien, no hay nada para sincronizar acá.
```
public class MovieTicketsAverager {
    public int tope;
    public List<Movie> movies;
    /** */
    public MovieTicketsAverager(int tope, List<Movie> movies) {
        super();
        this.tope = tope;
        this.movies = new ArrayList<>(movies);
    }
    public double average() {
        return movies.stream().filter(movie -> movie.getYear() > tope)
                .collect(Collectors.averagingInt(movie -> movie.getTicketsSold()));
    }
}
class Movie {
    private int year;
    private String title;
    private int ticketsSold;
    public Movie(int year, String title, int ticketsSold) {
        super();
        this.year = year;
        this.title = title;
        this.ticketsSold = ticketsSold;
    }
    public int getYear() {
        return year;
    }
    public String getTitle() {
        return title;
    }
    public int getTicketsSold() {
        return ticketsSold;
    }
}
```