import React from "react";
import { useState } from "react";

const FQ = (props) => {
    console.log(props);
    const [selectedFile, setSelectedFile] = useState();
	const [isFilePicked, setIsFilePicked] = useState(false);
    const [isSelected, setIsSelected] = useState(false);

	const changeHandler = (event) => {
		setSelectedFile(event.target.files[0]);
		setIsSelected(!isSelected);
        setIsFilePicked(true);
	};

	const handleSubmission = () => {
        if (isFilePicked) {
            console.log("File submitted.");
        }
    };
    return <div>
        <h1>Information Requested</h1>
        <p>Patient Name: {props.location.state.case.name}<br/>
        Record Number: Example Record Number<br/>
        Request Submitted by: {props.location.state.case.org}<br/>
        Information Details: {props.location.state.case.info}
        </p>
        <input type="file" name="file" onChange={changeHandler} />
        {isSelected ? (
				<div>
					<p>Filename: {selectedFile.name}</p>
					<p>Filetype: {selectedFile.type}</p>
					<p>Size in bytes: {selectedFile.size}</p>
					<p>
						lastModifiedDate:{' '}
						{selectedFile.lastModifiedDate.toLocaleDateString()}
					</p>
				</div>
			) : (
				<p>Select a file to show details</p>
			)}
			<div>
				<button onClick={(handleSubmission)}>Submit</button>
			</div>
    </div>;
}
export { FQ };

export default FQ;