import { UserRepository } from "./AuditService";

const service = new UserRepository();

service.save({
    users: [
        { userId: 'user id 1' },
        { userId: 'user id 2' },
    ]
});
