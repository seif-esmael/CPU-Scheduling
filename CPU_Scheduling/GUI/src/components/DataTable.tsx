import ColorBox from "./ColorBox";
import Style from "./DataTable.module.css";

interface Props {
  labels: string[];
  data: {
    [key: string]: string;
  }[];
}

const DataTable = ({ labels, data }: Props) => {
  console.log(data);
  return (
    <table className={Style.table}>
      <thead>
        <th>
          <td>NUMBER</td>
          {labels.map((label) => (
            <td>{label.toUpperCase()}</td>
          ))}
        </th>
      </thead>
      <tbody>
        {data.map((value, index) => (
          <tr>
            <td>{index}</td>
            {labels.map((label) => {
              if (label == "color")
                return (
                  <td>
                    <ColorBox color={value[label]} />
                  </td>
                );
              return <td>{value[label]}</td>;
            })}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default DataTable;
