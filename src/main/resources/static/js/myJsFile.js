// function congrats(){
//     $(document).ready(function () {
//         //      let notification = /*[[${notification}]]*/ "";
//         let action = document.getElementById('succeed');
//         console.log(action);
//         if (action.value === "You have successfully registered") {
//             Swal.fire({
//                 icon: 'success',
//                 title: 'Congratulations! You have successfully registered.',
//                 showConfirmButton: false,
//                 timer: 9000
//             })
//         }
//     })
// }

function congrats(){
    Swal.fire({
        icon: 'success',
        title: 'Congratulations! You have successfully registered.',
        showConfirmButton: false,
        timer: 9000
    })
}

function deleteSeller() {
    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire(
                'Deleted!',
                'Your file has been deleted.',
                'success'
            )
        }
    })
}

$('.confirmation').click(function (e) {
    let href = $(this).attr('href');

    swal({
            title: "Are you sure?",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Yes, delete it!",
            cancelButtonText: "No, cancel plx!",
            closeOnConfirm: true,
            closeOnCancel: true
        },
        function (isConfirm) {
            if (isConfirm) {
                window.location.href = href;
            }
        });

    return false;
});

function confirmation(){
    let href = $(this).attr('href');

    swal({
            title: "Are you sure?",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Yes, delete it!",
            cancelButtonText: "No, cancel plx!",
            closeOnConfirm: true,
            closeOnCancel: true
        },
        function (isConfirm) {
            if (isConfirm) {
                window.location.href = href;
            }
        });

    return false;
}