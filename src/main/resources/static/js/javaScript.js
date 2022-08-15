// /**
//  * It takes the value of the search input and sends it to the server.
//  */
// const search = () => {

//     let query = $("#search").val()

//     if (query == '') {
//         $(".search-result").hide();
//     } else {
//         console.log(query);
//         let url = `http://localhost:8082/search/${query}`;

//         /* A promise. It is a way to handle asynchronous code. */
//         fetch(url)
//             .then((response) => {
//                 return response.json();
//             })
//             .then((data) => {
//                 console.log(data)
//             });

//         $(".search-result").show();
//     }


// };