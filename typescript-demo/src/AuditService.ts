import { audited } from "./decorator";

interface UsersDTO {
    users: UserDTO[],
}

interface UserDTO {
    userId: string,
}

export class UserRepository {

    @audited('save', usersDTO => usersDTO.users.map(user => user.userId))
    save(users: UsersDTO) {
        console.log('saving users')
    }
}

// Auditiong action 'save', auditable id = user id 1,user id 2
// saving users
