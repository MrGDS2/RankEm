package build.free.mrgds2.rankem.Model

class Friend {
    var name: String? = null

    var image: String? = null

    constructor() {

    }

    constructor(name: String, profile_picture: String) {
        this.name = name
        this.image = profile_picture
    }


}
