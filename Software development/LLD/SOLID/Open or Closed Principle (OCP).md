
> [!tldr] Open for extension but closed for modification.

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

Now, Above code is already live and serving traffic. But, I now need to additionally save the invoice to file system / mongoDB

> [!caution]

```
class InvoiceDao {
    Invoice invoice;

    public InvoiceDao(Invoice invoice) {
        this.invoice = invoice;
    }

    public void saveToDB() {
        // Save Invoice into DB
    }

    public void saveToFile(String filename) {
        // Save Invoice in the File with the given name
    }
}
```

Here I am ***MODIFYING*** the existing tested class and it is now prone to bugs + It doesn't follow OCP.

> [!success]

```
interface InvoiceDao {
    public void save(Invoice invoice);
}

class DatabaseInvoiceDao implements InvoiceDao {

    @Override
    public void save(Invoice invoice) {
        // Save to DB
    }
}

class FileInvoiceDao implements InvoiceDao {

    @Override
    public void save(Invoice invoice) {
        // Save to file
    }
}
```

This is how we ***EXTEND*** the capability by creating an INTERFACE. We can now also add `MongoDBInvoiceDao` without ***MODIFYING*** others.
