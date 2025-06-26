interface Service {
    greet(): string,
}

class MyService implements Service {
    greet(): string {
        return 'hello';
    }
}

class AuditingServiceDecorator implements Service {

    constructor(private delegate: Service) {
    }

    greet(): string {
        return this.delegate.greet().toUpperCase();
    }
}

const service = new AuditingServiceDecorator(new MyService());

console.log(service.greet());
// HELLO
