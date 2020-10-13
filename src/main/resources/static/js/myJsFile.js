function congrats(){
    $(document).ready(function () {
        let action = document.getElementById('succeed');
        if (action.innerHTML === "You have successfully registered") {
            Swal.fire({
                icon: 'success',
                title: 'Congratulations! You have successfully registered.',
                showCancelButton: false,
                focusConfirm: false,
                html:
                    'Please click on, ' +
                    '<b><a href="/login">login</a></b> ' +
                    'to log into your page',
                confirmButtonColor: '#b1ef95',
                confirmButtonText:
                    '<a href="/login"><i class="fa fa-thumbs-up"></i> Great!</a>',
                confirmButtonAriaLabel: 'Thumbs up, great!'
            })
        }
    })
}

function created(){
    $(document).ready(function () {
        let action = document.getElementById('staff-added');
        if (action.innerHTML === "You have successfully added a staff") {
            Swal.fire({
                icon: 'success',
                title: 'Congrats! You have successfully added a staff.',
                showCancelButton: false,
                focusConfirm: false,
                confirmButtonColor: '#b1ef95',
                confirmButtonText:
                    '<a href="/staff/list"><i class="fa fa-thumbs-up"></i> Great!</a>',
                confirmButtonAriaLabel: 'Thumbs up, great!'
            })
        }
    })
}
