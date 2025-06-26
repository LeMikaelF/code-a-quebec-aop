from dataclasses import dataclass
from functools import wraps


def audited(action: str, extractor):
    def decorator(fn):
        @wraps(fn)
        def wrapper(*args, **kwargs):
            result = fn(*args, **kwargs)
            for tid in extractor(*args, **kwargs) or []:
                print(f"[AUDIT] {action} ➜ {tid}")
            return result

        return wrapper

    return decorator


@dataclass
class User:
    user_id: str
    name: str


@dataclass
class UsersDTO:
    users: list[User]


@audited("SAVE", lambda dto, *_: (u.user_id for u in dto.users))
def save(usersDTO: UsersDTO, payload):
    print("saving", payload)


if __name__ == "__main__":
    dto = UsersDTO(
        users=[
            User("user id 1", "Michael"),
            User("user id 2", "Mikaël"),
        ]
    )
    save(dto, "payload")

# saving payload
# [AUDIT] SAVE ➜ user id 1
# [AUDIT] SAVE ➜ user id 2
