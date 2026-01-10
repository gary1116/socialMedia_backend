# socialMedia_backend
a simple backend project to check how many to many relationships occur in real world projects where here I will be using jpa repository and h2 db 

# ONE TO ONE RELATIONSHIPS

- SocialUser

        package com.social.demo.models;
        import jakarta.persistence.Entity;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Id;
        
        @Entity
        public class SocialUser {
        
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
        
        }


- Profile

        package com.social.demo.models;

        import jakarta.persistence.*;
        
        @Entity
        public class Profile {
        
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
        
        
            @OneToOne
            @JoinColumn(name = "social_user")
            private SocialUser socialUser;
        
        }


here 
@OneToOne
@JoinColumn(name = "social_user")
private SocialUser socialUser;
🧠 Big picture (VERY IMPORTANT)
You are saying:

Each Profile is linked to exactly ONE SocialUser

That’s it.
This is a One-to-One relationship.

Think of it like:


SocialUser  <---->  Profile
One user → one profile
One profile → one user

🧩 What does @OneToOne actually mean?
1️⃣ @OneToOne (relationship definition)
java
Copy code
@OneToOne
private SocialUser socialUser;
This tells JPA:

“This field is NOT a normal column.
It represents a relationship to another table.”

Without this annotation:

JPA would treat SocialUser as a normal Java object

❌ That would NOT work in DB mapping

So:

@OneToOne =
👉 This entity is related to another entity in a one-to-one manner

🔗 What does @JoinColumn do?
java
Copy code
@JoinColumn(name = "social_user")
This answers WHERE and HOW the relationship is stored.

It tells JPA:
“Create a foreign key column in the profile table
that references social_user.id”

🗄️ Database tables created (VERY IMPORTANT)
🔹 social_user table
id
1
2

🔹 profile table (with @JoinColumn)
id	social_user
10	1
11	2

➡️ social_user is a FOREIGN KEY
➡️ It points to social_user.id

This is the actual link between the tables.

- Why is Profile owning the relationship?

        Because the foreign key is inside profile table.
        
        @JoinColumn(name = "social_user")
        
        
        ➡️ Profile owns the relationship
        ➡️ SocialUser does NOT need to know anything (yet)
        
        This is called the owning side


# BI-DIRECTIONAL MAPPING IN ONE TO ONE RELATIONSHIPS


wo entities that both know about each other:

SocialUser  <------>  SocialProfile


SocialProfile → knows its SocialUser

SocialUser → knows its SocialProfile

This is called a bidirectional relationship.

🧠 The MOST IMPORTANT RULE (memorize this)

Only ONE side owns the relationship

The owning side is the one that has the foreign key

In your code:

@JoinColumn(name = "social_user")
private SocialUser socialUser;


➡️ SocialProfile is the OWNING side
➡️ SocialUser is the INVERSE (non-owning) side

🔍 Let’s analyze each class separately
1️⃣ SocialProfile (OWNING SIDE)
@OneToOne
@JoinColumn(name = "social_user")
private SocialUser socialUser;

What this means:

This side:

✅ Creates the foreign key

✅ Controls the DB relationship

✅ Decides how rows are linked

Database result:

social_profile

id	social_user
1	10

➡️ social_user is a foreign key → social_user.id

🔥 This side writes to the database

2️⃣ SocialUser (INVERSE SIDE)
@OneToOne(mappedBy = "socialUser")
private SocialProfile socialProfile;

What does mappedBy = "socialUser" mean?

It literally means:

“I do NOT own this relationship.
The relationship is already mapped by the field
socialUser inside SocialProfile.”

So JPA understands:

❌ Do NOT create another foreign key

❌ Do NOT create another join table

✅ Just reuse the existing mapping

🧠 Why is mappedBy REQUIRED here?

Without mappedBy:

@OneToOne
private SocialProfile socialProfile;


JPA would think:

“Oh, another relationship!
I’ll create ANOTHER foreign key or join table 😈”

That leads to:

Duplicate mappings

Extra tables

Broken schema

Confusing bugs

mappedBy prevents that.

🗄️ Final database structure (VERY IMPORTANT)

Even after adding @OneToOne in SocialUser…

👉 DATABASE DOES NOT CHANGE

Still only two tables:

social_user

| id |

social_profile

| id | social_user (FK) |

✔ No new column
✔ No new table
✔ No duplicate foreign key

One-line mental model (interview gold)

@JoinColumn = owns the relationship
mappedBy = points to the owner

# ONE TO MANY RELATIONSHIP AND MANY TO ONE 

What tables/columns will be created?
social_user table

id (PK)

post table

id (PK)

user_id (FK → social_user.id) ✅ because of:

@ManyToOne
@JoinColumn(name="user_id")
private SocialUser socialUser;


So yes: user_id will be a column in post table and it will store the social_user.id.

🔥 Who “handles” (owns) the relationship?

In JPA:

The side with the foreign key is the owning side.

Here, the FK is in Post, so:

✅ Post.socialUser (@ManyToOne + @JoinColumn) = owning side

❌ SocialUser.post (@OneToMany(mappedBy="socialUser")) = inverse side

So your line:

“one to many relationship which is handled by Post class…”

✅ Correct: Post is the one that actually controls the DB link, because it contains the FK column.

What exactly does mappedBy = "socialUser" mean here?
@OneToMany(mappedBy = "socialUser")
private List<Post> post = new ArrayList<>();


This tells JPA:

“Don’t create another column/table for this.
The relationship is already stored in Post.socialUser.”

So JPA will NOT create:

a social_user_id column in social_user table (doesn’t make sense)

a join table like social_user_posts (that would happen if you didn’t use mappedBy)

✅ What happens at runtime when you save?
Important rule:

Only setting user.getPost().add(post) is not enough to persist the FK.

Because the inverse side (OneToMany) does not write the FK.

You must set the owning side:

post.setSocialUser(user);  // this sets user_id in post table


Best practice: keep both sides in sync using a helper:

public void addPost(Post p) {
post.add(p);
p.setSocialUser(this);
}

Your final question (answer)

“So JPA will see SocialUser class and see one-to-many relationship which is handled by Post class which will have column user_id in post table?”

✅ Yes — the @OneToMany in SocialUser is mainly for navigation (user → posts).
✅ The actual column user_id is created because of the @ManyToOne @JoinColumn in Post.

- Post -> SocialUser (owning side, writes FK)
- SocialUser -> List<Post> (inverse side, for navigation)



# MANY TO MANY RELATIONSHIP

The relationship is stored in a separate join table (user_group) that contains two foreign keys:

user_id → social_user.id

group_id → groups.id

And yes: SocialUser is the owning side in your code (because it defines @JoinTable). Groups is the inverse side (because it uses mappedBy).

- Now I’ll explain only the many-to-many part line by line. (check SocialUser and Groups class for reference)

SocialUser: Many-to-many part (line by line)
@ManyToMany


Says: one user can be in many groups AND one group can have many users.

@JoinTable(
name="user_group",


Tells JPA: create/use a join table named user_group (this table holds the mapping).

    joinColumns=@JoinColumn(name="user_id"),


This is the FK column in user_group that points to THIS entity’s table (social_user).

So user_group.user_id references social_user.id.

    inverseJoinColumns=@JoinColumn(name="group_id")


This is the FK column in user_group that points to the other entity’s table (groups).

So user_group.group_id references groups.id.

private Set<Groups> groups = new HashSet<>();


In Java: a user has a set of Groups (set avoids duplicates like same group added twice).

✅ Because SocialUser declares @JoinTable, this is the OWNING side.

Groups: Many-to-many part (line by line)
@ManyToMany(mappedBy = "groups")


mappedBy = "groups" means:

“The join table mapping is already defined on the other side — in SocialUser.groups.”
So Groups will NOT create another join table.
This side is inverse / non-owning side.
private Set<SocialUser> socialUsers = new HashSet<>();


In Java: a group has a set of users.

- Mental model you should keep

Entity PK → always @Id
FK column name → customizable with @JoinColumn
Linking logic → entity + PK, not column name
Defaults exist → annotations override them
- One-liner you can remember (interview safe)

“JPA links entities using primary keys, not column names —
@JoinColumn only controls how the foreign key column is named.”
