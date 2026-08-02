
> [!tldr] A class should have only one reason to change.


```
class Marker {
    String name;
    String color;
    int year;
    int price;

    public Marker(String name, String color, int year, int price) {
        this.name = name;
        this.color = color;
        this.year = year;
        this.price = price;
    }
}
```

> [!caution]

```
class Invoice {

    private Marker marker;
    private int quantity;

    public Invoice(Marker marker, int quantity) {
        this.marker = marker;
        this.quantity = quantity;
    }

    public int calculateTotal() {
        int price = ((marker.price) * this.quantity);
        return price;
    }

    public void printInvoice() {
        //print the Invoice
    }

    public void saveToDB() {
        // Save the data into DB
    }
}
```

Now, above `Invoice` class doesn't follow SRP as it has 3 responsibilities - calculateTotal, print & save, **so it has 3 reasons to change**. 

I might need to update:
- saveToDB logic to save to file
- print invoice in a different format
- add gst in the calculation

so below is the correct way to implement it.

> [!success]

```
class Invoice {

    private Marker marker;
    private int quantity;

    public Invoice(Marker marker, int quantity) {
        this.marker = marker;
        this.quantity = quantity;
    }

    public int calculateTotal() {
        int price = ((marker.price) * this.quantity);
        return price;
    }
}
```

```
class InvoiceDao {
    Invoice invoice;

    public InvoiceDao(Invoice invoice) {
        this.invoice = invoice;
    }

    public void saveToDB() {
        // Save into the DB
    }
}
```

```
class InvoicePrinter {
    private Invoice invoice;

    public InvoicePrinter(Invoice invoice) {
        this.invoice = invoice;
    }

    public void print() {
        //print the invoice
    }
}
```

***Now each class has 1 responsibility***
