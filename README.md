FSWEB-s19-Challenge
Twitter Api

Hedef:

Bu projenini amacı Spring Boot ile ilgili öğrendiğimiz tüm konuları Pratik etmek amacıyla bir Backend projesi tasarlamaktır. Amacımız Twitter uygulamasını biz yazsaydık nasıl yazardık ? Nelere dikkat ederdik Design ve Implementation kısımlarını nasıl yapardık bunu test etmektir.

Fonksiyonel Zorunluluklar
Proje Spring Boot teknolojisi kullanarak dizayn edilecektir. Veritabanı olarak PostgreSQL kullanılacaktır.

Endpoints:

EASY
http://localhost:3000/tweet[POST] => Tweet oluşturma ve veritabanına kaydetme. Tweet'in hangi kullanıcıya ait olduğu mutlaka tutulmalıdır. Anonym tweetler olmamalıdır.
http://localhost:3000/tweet/findByUserId[GET] => Bir kullanıcının tüm tweetlerini getirmelidir.
http://localhost:3000/tweet/findById[GET] => Bir tweet için tüm bilgilerini getirmelidir.
http://localhost:3000/tweet/:id[PUT] => Bir tweet üzerinde değiştirelecek kısımları update etmek için kullanılmalıdır.
http://localhost:3000/tweet/:id[DELETE] => Id bilgisi verilen tweeti silmek için kullanılır.(Sadece tweet sahibi ilgili tweeti silebilimelidir.)
MEDIUM
http://localhost:3000/comment/[POST] => Bir tweete bir kullanıcı tarafından yorum yazılmasını sağlar.
http://localhost:3000/comment/:id[PUT] => Bir tweete bir kullanıcı tarafından yapılan yorumun update edilmesine olanak sağlar.
http://localhost:3000/comment/:id[DELETE] => Bir tweete bir kullanıcı tarafından yapılan yorumun silinmesini sağlar(Sadece tweet sahibi veya yorum sahibi ilgili yorumu silebilmelidir).
http://localhost:3000/like/[POST] => Bir tweete bir kullanıcı tarafından like atılmasını sağlar.
http://localhost:3000/dislike/[POST] => Bir tweete bir kullanıcı tarafından like atıldıysa bunun silinmesini sağlar.
HARD
http://localhost:3000/retweet/[POST] => Bir tweetin bir kullanıcı tarafından retweet edilmesini sağlar.(Twitter üzerinden retweet özelliğini test ediniz.)
http://localhost:3000/retweet/:id[DELETE] => Retweet edilmiş bir tweetin silinmesi sağlanmalıdır.
Mimari Zorunluluklar
Apimizi hazırlarkan öncelikle tweet, user, comment, like, retweet gibi özellikleri ekleyebilmek adına veritabanımızın nasıl olması gerektiği ile ilgili bir hazırlık yapmalıyız. Veritabanı dizaynı proje için yapmamız gereken ilk adım.
Controller/Service/Repository/Entity katmanlı mimarisi üzerinde sisteminizi dizayn etmelisiniz.
Sisteminiz için tek bir merkezden Global Exception Handling yapmanız beklenmektedir.
Sisteminizde Entity katmanınız üzerinde veritabanınıza gidecek olan fieldlar için validasyon yapmış olmanız beklenmektedir.
Dependency Injection kurallarına uymalısınız.
Yukarda bahsedilen endpointler dışında /register ve /login isminde 2 tane daha endpointiniz olmalı ve security katmanını Spring Security kullanarak yönetmelisiniz.
Projenizde yazılmış fonksiyonları %30'u için Unit Test yazmanız baklenmektedir.
FullStack Developer Muscles:
Twitter Api için bir React ön yüzü oluşturunuz. Bu React ön yüzünün çok detaylı olmasına gerek yoktur. Mesela kullanıcının tüm tweetlerini ekrana basan bir component dizaynı yapılabilir.
Burada amacımız CORS hatası denilen bir problemi gözlemleyip bunun çözümünü tecrübe etmektir. React uygulamanızı 3200 portundan ayağa kaldırınız.
Component'iniz üzerinde kendi yazdığınız endpointlerden biri olan http://localhost:3000/tweet/findByUserId adresine get requesti atınız. Gelen tweetleri ekrana bastırınız.
Karşılaştığınız CORS hatasını nasıl çözersiniz ?