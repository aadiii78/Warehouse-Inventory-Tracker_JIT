# 🏭 Warehouse Inventory Tracker (Event-Based)

A *Java-based warehouse management system* that simulates real-time *inventory tracking* using *event-driven programming (Observer Pattern), **multithreading, and **file persistence*.

This console-based app allows you to manage products, receive shipments, fulfill orders, and automatically trigger low-stock alerts.

---

## 🚀 Features

✅ *Add New Products Dynamically*  
✅ *Event-Based Low Stock Alerts (Observer Pattern)*  
✅ *Thread-Safe Operations (Multithreading)*  
✅ **Persistent Storage (Saved in inventory.txt)**   
✅ *Menu-Driven Console Interface with Exit Option*

---

## 🧩 Class Structure

| File | Description |
|------|--------------|
| Product.java | Represents a product with ID, name, quantity, and reorder threshold. |
| StockObserver.java | Interface for alert mechanism. |
| AlertService.java | Concrete implementation that displays alerts in the console. |
| Warehouse.java | Core class handling product management, stock updates, file I/O, and event triggers. |
| WarehouseApp.java | Main entry point – handles user interaction and runs operations in threads. |

---


