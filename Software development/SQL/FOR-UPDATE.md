#sql


A **row-level pessimistic lock** taken during a `SELECT`, which tells the database:

> “I’m about to modify these rows — block everyone else from touching them until I’m done.”

Other transactions can usually do normal `SELECT` (depends on isolation level)

Lock is released only on `COMMIT` / `ROLLBACK`