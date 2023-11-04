import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { Application } from "./types";
import styles from "./Profile.module.css"

const columns: GridColDef[] = [
    { field: 'universityName', headerName: 'University name', width: 300 },
    { field: 'universityCourse', headerName: 'University  course', width: 300 },
];

interface Props {
    applications: Application[]
}

export default function ApplicationList(props: Props) {

    const { applications } = props

    if (!applications || applications.length <= 0) return null

    return <div className={styles.grid}>
        <DataGrid
            rows={applications}
            columns={columns}
            initialState={{
                pagination: {
                    paginationModel: { page: 0, pageSize: 5 },
                },
            }}
            pageSizeOptions={[5, 10, 25]}

        />

    </div>


}