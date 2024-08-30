export function handleError(error) {
    if (!error.response || !error.response.data) {
        console.log(error.message);
        return "서버와의 통신이 원활하지 않습니다. 잠시 후 다시 시도해주세요.";
    }

    const errorMessage = error.response.data;

    if (errorMessage.status) {
        return errorMessage.message;
    } else {
        return "서버와의 통신이 원활하지 않습니다. 잠시 후 다시 시도해주세요.";
    }
}
