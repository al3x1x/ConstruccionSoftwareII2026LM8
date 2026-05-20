// Inicializar colecciones y índices en MongoDB
db = db.getSiblingDB('bank_db');

// Crear colecciones si no existen
db.createCollection('users');
db.createCollection('bank_accounts');
db.createCollection('loans');
db.createCollection('transfers');
db.createCollection('audit_logs');

// Crear índices para mejor performance
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "role": 1 });

db.bank_accounts.createIndex({ "holderId": 1 });
db.bank_accounts.createIndex({ "status": 1 });

db.loans.createIndex({ "clientId": 1 });
db.loans.createIndex({ "status": 1 });

db.transfers.createIndex({ "originAccount": 1 });
db.transfers.createIndex({ "destinationAccount": 1 });
db.transfers.createIndex({ "status": 1 });

db.audit_logs.createIndex({ "userId": 1 });
db.audit_logs.createIndex({ "operationType": 1 });
db.audit_logs.createIndex({ "timestamp": -1 });

// Crear usuario para la aplicación
db.createUser({
  user: 'bank_user',
  pwd: 'bank_password',
  roles: [
    {
      role: 'readWrite',
      db: 'bank_db'
    }
  ]
});

print("✅ MongoDB bank_db inicializada correctamente");
